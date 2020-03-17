/**
 * 
 */
package com.github.mfds2j.classgen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Andrew
 *
 */
@Deprecated
public class VelocityGenerator {
	VelocityContext context;
	VelocityEngine velocityEngine;
	Template template;

	/**
	 * 
	 */
	public VelocityGenerator(Properties properties) {
		this.velocityEngine = new VelocityEngine();

		// These properties tell Velocity to use its own classpath-based
		// loader, then drop down to check the root and the current folder
		velocityEngine.addProperty("resource.loader", "class, file");
		velocityEngine.addProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.addProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		velocityEngine.addProperty("file.resource.loader.path", "/, .");
		velocityEngine.setProperty("runtime.references.strict", true);

		// Set whitespace gobbling to Backward Compatible (BC)
		// https://velocity.apache.org/engine/2.0/developer-guide.html#space-gobbling
		velocityEngine.setProperty("space.gobbling", "bc");

		Velocity.init(properties);
		template = Velocity.getTemplate("IVirtual.vm");
		velocityEngine = new VelocityEngine(properties);
		context = new VelocityContext();
	}

	public static final String CLASS_PROP = "java-class";
	public static final String KEY_CLASS_PROP = "java-key-class";
	public static final String ELEMENT_PROP = "java-element-class";

	/**
	 * List of Java reserved words from
	 * https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.9 combined
	 * with the boolean and null literals. combined with the classnames used
	 * internally in the generated MFDS2J code.
	 */
	public static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList("abstract", "assert", "boolean",
			"break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else",
			"enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import",
			"instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected",
			"public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw",
			"throws", "transient", "true", "try", "void", "volatile", "while"));

	/**
	 * Read/write some common builtin classes as strings. Representing these as
	 * strings isn't always best, as they aren't always ordered ideally, but at
	 * least they're stored. Also note that, for compatibility, only classes that
	 * wouldn't be otherwise correctly readable or writable should be added here,
	 * e.g., those without a no-arg constructor or those whose fields are all
	 * transient.
	 */
	protected Set<Class<?>> stringableClasses = new HashSet<>();
	{
		stringableClasses.add(java.math.BigDecimal.class);
		stringableClasses.add(java.math.BigInteger.class);
		stringableClasses.add(java.net.URI.class);
		stringableClasses.add(java.net.URL.class);
		stringableClasses.add(java.io.File.class);
	}

	/* Reserved words for accessor/mutator methods */
	private static final Set<String> ACCESSOR_MUTATOR_RESERVED_WORDS = new HashSet<>(
			Arrays.asList("class", "schema", "classSchema"));
	static {
		// Add reserved words to accessor/mutator reserved words
		ACCESSOR_MUTATOR_RESERVED_WORDS.addAll(RESERVED_WORDS);
	}
	/* Reserved words for error types */
	private static final Set<String> ERROR_RESERVED_WORDS = new HashSet<>(Arrays.asList("message", "cause"));
	static {
		// Add accessor/mutator reserved words to error reserved words
		ERROR_RESERVED_WORDS.addAll(ACCESSOR_MUTATOR_RESERVED_WORDS);
	}

	private static final String FILE_HEADER = "/**\n" + " * Autogenerated by Avro\n" + " *\n"
			+ " * DO NOT EDIT DIRECTLY\n" + " */\n";

	/** Utility for template use. Adds a dollar sign to reserved words. */
	  public static String mangle(String word) {
	    return mangle(word, false);
	  }

	  /** Utility for template use. Adds a dollar sign to reserved words. */
	  public static String mangle(String word, boolean isError) {
	    return mangle(word, isError ? ERROR_RESERVED_WORDS : RESERVED_WORDS);
	  }

	  /** Utility for template use. Adds a dollar sign to reserved words. */
	  public static String mangle(String word, Set<String> reservedWords) {
	    return mangle(word, reservedWords, false);
	  }

	  /** Utility for template use. Adds a dollar sign to reserved words. */
	  public static String mangle(String word, Set<String> reservedWords, boolean isMethod) {
	    if (word.contains(".")) {
	      // If the 'word' is really a full path of a class we must mangle just the
	      // classname
	      int lastDot = word.lastIndexOf(".");
	      String packageName = word.substring(0, lastDot + 1);
	      String className = word.substring(lastDot + 1);
	      return packageName + mangle(className, reservedWords, isMethod);
	    }
	    if (reservedWords.contains(word) || (isMethod && reservedWords
	        .contains(Character.toLowerCase(word.charAt(0)) + ((word.length() > 1) ? word.substring(1) : "")))) {
	      return word + "$";
	    }
	    return word;
	  }
	
}