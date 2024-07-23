package org.iglooproject.autoprefixer;

/**
 * Autoprefix utility interface. See {@link Autoprefixer} to obtain an implementation.
 *
 * @see Autoprefixer
 */
public interface IAutoprefixer {

  /**
   * Process a CSS given as a string with autoprefixer. Throw an {@link AutoprefixerException} is an
   * error is encountered processing the css.
   *
   * @param css
   * @return css processed by autoprefixer
   * @throws AutoprefixerException
   */
  public String process(String css) throws AutoprefixerException;
}
