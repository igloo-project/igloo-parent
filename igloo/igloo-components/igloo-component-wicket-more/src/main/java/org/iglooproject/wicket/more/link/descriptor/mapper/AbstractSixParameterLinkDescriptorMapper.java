package org.iglooproject.wicket.more.link.descriptor.mapper;

import igloo.wicket.model.Models;
import org.apache.wicket.model.IModel;
import org.javatuples.Quintet;
import org.javatuples.Sextet;

public abstract class AbstractSixParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5, T6>
    implements ISixParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5, T6> {
  private static final long serialVersionUID = 1271669480797343858L;

  @Override
  public void detach() {}

  @Override
  public abstract L map(
      Sextet<
              ? extends IModel<T1>,
              ? extends IModel<T2>,
              ? extends IModel<T3>,
              ? extends IModel<T4>,
              ? extends IModel<T5>,
              ? extends IModel<T6>>
          param);

  @Override
  public final L map(
      IModel<T1> model1,
      IModel<T2> model2,
      IModel<T3> model3,
      IModel<T4> model4,
      IModel<T5> model5,
      IModel<T6> model6) {
    return map(Sextet.with(model1, model2, model3, model4, model5, model6));
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<L, T2, T3, T4, T5, T6> setParameter1(
      final IModel<T1> model1) {
    return new AbstractFiveParameterLinkDescriptorMapper<L, T2, T3, T4, T5, T6>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quintet<
                  ? extends IModel<T2>,
                  ? extends IModel<T3>,
                  ? extends IModel<T4>,
                  ? extends IModel<T5>,
                  ? extends IModel<T6>>
              param) {
        return AbstractSixParameterLinkDescriptorMapper.this.map(param.addAt0(model1));
      }

      @Override
      public void detach() {
        super.detach();
        model1.detach();
        AbstractSixParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<L, T2, T3, T4, T5, T6> ignoreParameter1() {
    return setParameter1(Models.<T1>placeholder());
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T3, T4, T5, T6> setParameter2(
      final IModel<T2> model2) {
    return new AbstractFiveParameterLinkDescriptorMapper<L, T1, T3, T4, T5, T6>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quintet<
                  ? extends IModel<T1>,
                  ? extends IModel<T3>,
                  ? extends IModel<T4>,
                  ? extends IModel<T5>,
                  ? extends IModel<T6>>
              param) {
        return AbstractSixParameterLinkDescriptorMapper.this.map(param.addAt1(model2));
      }

      @Override
      public void detach() {
        super.detach();
        model2.detach();
        AbstractSixParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T3, T4, T5, T6> ignoreParameter2() {
    return setParameter2(Models.<T2>placeholder());
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T4, T5, T6> setParameter3(
      final IModel<T3> model3) {
    return new AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T4, T5, T6>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quintet<
                  ? extends IModel<T1>,
                  ? extends IModel<T2>,
                  ? extends IModel<T4>,
                  ? extends IModel<T5>,
                  ? extends IModel<T6>>
              param) {
        return AbstractSixParameterLinkDescriptorMapper.this.map(param.addAt2(model3));
      }

      @Override
      public void detach() {
        super.detach();
        model3.detach();
        AbstractSixParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T4, T5, T6> ignoreParameter3() {
    return setParameter3(Models.<T3>placeholder());
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T5, T6> setParameter4(
      final IModel<T4> model4) {
    return new AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T5, T6>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quintet<
                  ? extends IModel<T1>,
                  ? extends IModel<T2>,
                  ? extends IModel<T3>,
                  ? extends IModel<T5>,
                  ? extends IModel<T6>>
              param) {
        return AbstractSixParameterLinkDescriptorMapper.this.map(param.addAt3(model4));
      }

      @Override
      public void detach() {
        super.detach();
        model4.detach();
        AbstractSixParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T5, T6> ignoreParameter4() {
    return setParameter4(Models.<T4>placeholder());
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T6> setParameter5(
      final IModel<T5> model5) {
    return new AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T6>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quintet<
                  ? extends IModel<T1>,
                  ? extends IModel<T2>,
                  ? extends IModel<T3>,
                  ? extends IModel<T4>,
                  ? extends IModel<T6>>
              param) {
        return AbstractSixParameterLinkDescriptorMapper.this.map(param.addAt4(model5));
      }

      @Override
      public void detach() {
        super.detach();
        model5.detach();
        AbstractSixParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T6> ignoreParameter5() {
    return setParameter5(Models.<T5>placeholder());
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5> setParameter6(
      final IModel<T6> model6) {
    return new AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5>() {
      private static final long serialVersionUID = 1L;

      @Override
      public L map(
          Quintet<
                  ? extends IModel<T1>,
                  ? extends IModel<T2>,
                  ? extends IModel<T3>,
                  ? extends IModel<T4>,
                  ? extends IModel<T5>>
              param) {
        return AbstractSixParameterLinkDescriptorMapper.this.map(param.addAt5(model6));
      }

      @Override
      public void detach() {
        super.detach();
        model6.detach();
        AbstractSixParameterLinkDescriptorMapper.this.detach();
      }
    };
  }

  @Override
  public AbstractFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5> ignoreParameter6() {
    return setParameter6(Models.<T6>placeholder());
  }
}
