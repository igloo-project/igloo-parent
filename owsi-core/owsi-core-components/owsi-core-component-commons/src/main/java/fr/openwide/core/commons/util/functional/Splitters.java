package fr.openwide.core.commons.util.functional;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

import fr.openwide.core.commons.util.functional.SerializableFunction;

public final class Splitters {

	private static final Splitter SPACE = Splitter.on(" ").trimResults().omitEmptyStrings();
	private static final Splitter NON_BREAKING_SPACE = Splitter.on("\u00A0").trimResults().omitEmptyStrings();
	private static final Splitter NEW_LINE = Splitter.on("\n").trimResults().omitEmptyStrings();
	private static final Splitter NEW_LINE_SEMICOLON = Splitter.on(" ;\n").trimResults().omitEmptyStrings();
	private static final Splitter HYPHEN_SPACE = Splitter.on(" - ").trimResults().omitEmptyStrings();
	private static final Splitter MIDDOT_SPACE = Splitter.on(" · ").trimResults().omitEmptyStrings();
	private static final Splitter DOT = Splitter.on(".").trimResults().omitEmptyStrings();
	private static final Splitter SLASH = Splitter.on("/").trimResults().omitEmptyStrings();
	private static final Splitter COMMA = Splitter.on(",").trimResults().omitEmptyStrings();

	public static Splitter onSpace() {
		return SPACE;
	}

	public static Splitter onNonBreakingSpace() {
		return NON_BREAKING_SPACE;
	}

	public static Splitter onNewLine() {
		return NEW_LINE;
	}

	public static Splitter onNewLineSemicolon() {
		return NEW_LINE_SEMICOLON;
	}

	public static Splitter onHyphenSpace() {
		return HYPHEN_SPACE;
	}

	public static Splitter onMiddotSpace() {
		return MIDDOT_SPACE;
	}

	public static Splitter onDot() {
		return DOT;
	}

	public static Splitter onSlash() {
		return SLASH;
	}

	public static Splitter onComma() {
		return COMMA;
	}

	public static final class Functions {

		private static final Function<Object, Splitter> SPACE = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.SPACE;
			}
		};
		private static final Function<Object, Splitter> NON_BREAKING_SPACE = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.NON_BREAKING_SPACE;
			}
		};
		private static final Function<Object, Splitter> NEW_LINE = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.NEW_LINE;
			}
		};
		private static final Function<Object, Splitter> NEW_LINE_SEMICOLON = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.NEW_LINE_SEMICOLON;
			}
		};
		private static final Function<Object, Splitter> HYPHEN_SPACE = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.HYPHEN_SPACE;
			}
		};
		private static final Function<Object, Splitter> MIDDOT_SPACE = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.MIDDOT_SPACE;
			}
		};
		private static final Function<Object, Splitter> DOT = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.DOT;
			}
		};
		private static final Function<Object, Splitter> SLASH = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.SLASH;
			}
		};
		private static final Function<Object, Splitter> COMMA = new SerializableFunction<Object, Splitter>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Splitter apply(Object input) {
				return Splitters.COMMA;
			}
		};
		
		public static Function<Object, Splitter> onSpace() {
			return SPACE;
		}

		public static Function<Object, Splitter> onNonBreakingSpace() {
			return NON_BREAKING_SPACE;
		}

		public static Function<Object, Splitter> onNewLine() {
			return NEW_LINE;
		}

		public static Function<Object, Splitter> onNewLineSemicolon() {
			return NEW_LINE_SEMICOLON;
		}

		public static Function<Object, Splitter> onHyphenSpace() {
			return HYPHEN_SPACE;
		}

		public static Function<Object, Splitter> onMiddotSpace() {
			return MIDDOT_SPACE;
		}

		public static Function<Object, Splitter> onDot() {
			return DOT;
		}

		public static Function<Object, Splitter> onSlash() {
			return SLASH;
		}

		public static Function<Object, Splitter> onComma() {
			return COMMA;
		}

		private Functions() {
		}
	}

	private Splitters() {
	}

}
