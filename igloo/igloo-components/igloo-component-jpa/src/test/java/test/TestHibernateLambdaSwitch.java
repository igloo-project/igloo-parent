package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.assertj.core.data.Percentage;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityImplementation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

class TestHibernateLambdaSwitch {

	/**
	 * This method allows to check performance overhead introduced by lambda-implementation of
	 * {@link GenericEntity#equals(Object)}.
	 */
	@Test
	void testEqualsSwitch() throws IllegalAccessException, ClassNotFoundException, IOException,
			NoSuchMethodException, SecurityException {
		assumeThat(Thread.currentThread().getContextClassLoader()).isInstanceOf(URLClassLoader.class);
		
		// enough loop so that any slow down (gc, ...) does not 
		int nLoop = 500;
		// enough object so that twice by twice comparison is long enough
		// not too much so that memory management does not impact timings
		int nbObjects = 500;
		long randomSeed = 17386935l;
		int skippedLongestLoops = 4;
		Percentage withinPercentage = Percentage.withPercentage(5);
		List<Long> newImplementationHibernate;
		List<Long> oldImplementationHibernate;
		List<Long> newImplementationSimple;
		List<Long> oldImplementationSimple;
		
		// first loop : new implementation with hibernate code
		newImplementationHibernate = doWithClassLoader(classLoaderSupplier(true), () -> {
			return doEqualsSwitch(nbObjects, randomSeed, nLoop, TestEntity.class.getName(), GenericEntityImplementation.HIBERNATE);
		});
		
		// second loop : old implementation; equals method overriden in TestEntityHibernate
		oldImplementationHibernate = doWithClassLoader(classLoaderSupplier(true), () -> {
			return doEqualsSwitch(nbObjects, randomSeed, nLoop, TestEntityHibernate.class.getName(), GenericEntityImplementation.HIBERNATE);
		});
		
		// third loop : new implementation without hibernate
		newImplementationSimple = doWithClassLoader(classLoaderSupplier(false), () -> {
			return doEqualsSwitch(nbObjects, randomSeed, nLoop, TestEntity.class.getName(), GenericEntityImplementation.SIMPLE);
		});
		
		// forth loop : old implementation without hibernate
		oldImplementationSimple = doWithClassLoader(classLoaderSupplier(false), () -> {
			return doEqualsSwitch(nbObjects, randomSeed, nLoop, TestEntitySimple.class.getName(), GenericEntityImplementation.SIMPLE);
		});
		
		// skip first loops for calculations
		Function<List<Long>, Long> avg = (newTimings) ->
			newTimings.stream().sorted().skip(skippedLongestLoops).reduce(Long::sum).get() / newTimings.size();
		long newImplementationHibernateTiming = avg.apply(newImplementationHibernate);
		long oldImplementationHibernateTiming = avg.apply(oldImplementationHibernate);
		long newImplementationSimpleTiming = avg.apply(newImplementationSimple);
		long oldImplementationSimpleTiming = avg.apply(oldImplementationSimple);
		
		Logger logger = LoggerFactory.getLogger(TestHibernateLambdaSwitch.class);
		logger.warn("New implementation / hibernate: {} µs", newImplementationHibernateTiming);
		logger.warn("Old implementation / hibernate: {} µs", oldImplementationHibernateTiming);
		logger.warn("New implementation / simple: {} µs", newImplementationSimpleTiming);
		logger.warn("Old implementation / simple: {} µs", oldImplementationSimpleTiming);
		
		// check that timings are either less or within 10% old value
		try {
			assertThat(newImplementationHibernateTiming).isLessThan(oldImplementationHibernateTiming);
		} catch (AssertionError e) {
			assertThat(newImplementationHibernateTiming).isCloseTo(oldImplementationHibernateTiming, withinPercentage);
		}
		try {
			assertThat(newImplementationSimpleTiming).isLessThan(oldImplementationSimpleTiming);
		} catch (AssertionError e) {
			assertThat(newImplementationSimpleTiming).isCloseTo(oldImplementationSimpleTiming, withinPercentage);
		}
	}

	public List<Long> doEqualsSwitch(int nbObjects, long randomSeed, int nLoop, String className, GenericEntityImplementation implementation) {
		try {
			// we cannot reference classes by names are we want new classes instances to be loaded by overriden
			// class-loader
			List<Object> myEntities = new ArrayList<>();
			Random r = new Random(randomSeed);
			Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
			Method method1 = clazz.getMethod("setId", Long.class);
			Enum<?> implementationField = (Enum<?>) clazz.getField("IMPLEMENTATION").get(clazz);
			
			// check current implementation
			// we must use name as enum class is loaded with different class loaders
			assumeThat(implementationField.name()).isEqualTo(implementation.name());
			
			// build objects
			IntStream.range(0, nbObjects).forEach((i) -> {
				try {
					Object entity;
					entity = clazz.getConstructor().newInstance();
					method1.invoke(entity, r.nextLong());
					myEntities.add(entity);
				} catch (NoSuchMethodException |InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new IllegalStateException(e);
				}
			});
			
			List<Long> results = new ArrayList<Long>(nLoop);
			// compare objects for n loops
			for (int loop = 0; loop < nLoop; loop++) {
				Stopwatch watch = Stopwatch.createStarted();
				for (Object entity1 : myEntities) {
					for (Object entity2 : myEntities) {
						entity1.equals(entity2);
					}
				}
				watch.stop();
				results.add(watch.elapsed(TimeUnit.MICROSECONDS));
			}
			
			return results;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
			throw new IllegalStateException(e);
		}
	}

	public Supplier<ClassLoader> classLoaderSupplier(boolean includeHibernate) throws ClassNotFoundException, IOException {
		URLClassLoader myClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
		URL[] urls;
		if ( ! includeHibernate) {
			urls = Arrays.stream(myClassLoader.getURLs())
					.filter((i) -> ! i.getFile().contains("hibernate"))
					.<URL>toArray(i -> new URL[i]);
			assumeThat(urls.length).isNotEqualTo(myClassLoader.getURLs().length);
		} else {
			urls = myClassLoader.getURLs();
		}
		
		return () -> new URLClassLoader(urls, null);
	}

	@SuppressWarnings("unchecked")
	public Class<TestEntity> loadTestEntity() throws ClassNotFoundException, IOException {
		try (URLClassLoader classLoader = new URLClassLoader(((URLClassLoader) getClass().getClassLoader()).getURLs(), null)) {
			return (Class<TestEntity>) Class.forName(TestEntity.class.getName(), true, classLoader);
		}
	}

	public List<Long> doWithClassLoader(Supplier<ClassLoader> classLoaderSupplier, Supplier<List<Long>> r) {
		ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader classLoader = classLoaderSupplier.get();
		try {
			Thread.currentThread().setContextClassLoader(classLoader);
			return r.get();
		} finally {
			if (classLoader instanceof Closeable) {
				try {
					((Closeable) classLoader).close();
				} catch (IOException e) {
					throw new IllegalStateException(String.format("Error closing classLoader %s", classLoader), e);
				}
			}
			Thread.currentThread().setContextClassLoader(previousClassLoader);
		}
	}

}
