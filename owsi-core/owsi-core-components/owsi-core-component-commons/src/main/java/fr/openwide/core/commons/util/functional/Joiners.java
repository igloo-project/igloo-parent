package fr.openwide.core.commons.util.functional;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

import fr.openwide.core.commons.util.functional.SerializableFunction;

public final class Joiners {

	private static final Joiner SPACE = Joiner.on(" ").skipNulls();
	private static final Joiner NON_BREAKING_SPACE = Joiner.on("\u00A0").skipNulls();
	private static final Joiner NEW_LINE = Joiner.on("\n").skipNulls();
	private static final Joiner NEW_LINE_SEMICOLON = Joiner.on(" ;\n").skipNulls();
	private static final Joiner HYPHEN_SPACE = Joiner.on(" - ").skipNulls();
	private static final Joiner MIDDOT_SPACE = Joiner.on(" · ").skipNulls();
	private static final Joiner DOT = Joiner.on(".").skipNulls();
	private static final Joiner SLASH = Joiner.on("/").skipNulls();
	private static final Joiner COMMA = Joiner.on(", ").skipNulls();
	
	public static Joiner onSpace() {
		return SPACE;
	}

	public static Joiner onNonBreakingSpace() {
		return NON_BREAKING_SPACE;
	}

	public static Joiner onNewLine() {
		return NEW_LINE;
	}

	public static Joiner onNewLineSemicolon() {
		return NEW_LINE_SEMICOLON;
	}

	public static Joiner onHyphenSpace() {
		return HYPHEN_SPACE;
	}

	public static Joiner onMiddotSpace() {
		return MIDDOT_SPACE;
	}

	public static Joiner onDot() {
		return DOT;
	}

	public static Joiner onSlash() {
		return SLASH;
	}

	public static Joiner onComma() {
		return COMMA;
	}

	public static final class Functions {

		private static final Function<Object, Joiner> SPACE = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.SPACE;
			}
		};
		private static final Function<Object, Joiner> NON_BREAKING_SPACE = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.NON_BREAKING_SPACE;
			}
		};
		private static final Function<Object, Joiner> NEW_LINE = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.NEW_LINE;
			}
		};
		private static final Function<Object, Joiner> NEW_LINE_SEMICOLON = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.NEW_LINE_SEMICOLON;
			}
		};
		private static final Function<Object, Joiner> HYPHEN_SPACE = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.HYPHEN_SPACE;
			}
		};
		private static final Function<Object, Joiner> MIDDOT_SPACE = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.MIDDOT_SPACE;
			}
		};
		private static final Function<Object, Joiner> DOT = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.DOT;
			}
		};
		private static final Function<Object, Joiner> SLASH = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.SLASH;
			}
		};
		private static final Function<Object, Joiner> COMMA = new SerializableFunction<Object, Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner apply(Object input) {
				return Joiners.COMMA;
			}
		};
		
		public static Function<Object, Joiner> onSpace() {
			return SPACE;
		}

		public static Function<Object, Joiner> onNonBreakingSpace() {
			return NON_BREAKING_SPACE;
		}

		public static Function<Object, Joiner> onNewLine() {
			return NEW_LINE;
		}

		public static Function<Object, Joiner> onNewLineSemicolon() {
			return NEW_LINE_SEMICOLON;
		}

		public static Function<Object, Joiner> onHyphenSpace() {
			return HYPHEN_SPACE;
		}

		public static Function<Object, Joiner> onMiddotSpace() {
			return MIDDOT_SPACE;
		}

		public static Function<Object, Joiner> onDot() {
			return DOT;
		}

		public static Function<Object, Joiner> onSlash() {
			return SLASH;
		}

		public static Function<Object, Joiner> onComma() {
			return COMMA;
		}

		private Functions() {
		}
	}

	private Joiners() {
	}

}
