/**
 * 
 */
package com.github.cobolio.internal.compiler;

import org.apache.velocity.app.VelocityEngine;

import com.github.cobolio.CobolSchema;

/**
 * @author Andrew
 *
 */
public class CobolIOCompiler {
	private static final int JVM_METHOD_ARG_LIMIT = 255;
	public enum FieldVisibility {
		PUBLIC, PUBLIC_DEPRECATED, PRIVATE;
	}
	
	public enum PrimitiveTypes {
		STRING, INTEGER, DOUBLE, 
	}
	
	private VelocityEngine velocityEngine;
	private String templateDir;
	private FieldVisibility fieldVisibility = FieldVisibility.PRIVATE;
	private boolean createOptionalGetters = false;
	private boolean gettersReturnOptional = false;
	private boolean createSetters = true;
	private boolean createAllArgsConstructor = true;
	private String outputCharacterEncoding;
	private String suffix = ".java";
	
	private static final String FILE_HEADER = 
					  "/**\n" 
					+ " * Autogenerated by CobolIO\n"
					+ " *\n"
					+ " * DO NOT EDIT DIRECTLY\n"
					+ " */\n";
	
	public CobolIOCompiler(CobolSchema schema) {
		enqueue(schema);
	}
	
	/**
	   * Set the resource directory where templates reside. First, the compiler checks
	   * the system path for the specified file, if not it is assumed that it is
	   * present on the classpath.
	   */
	  public void setTemplateDir(String templateDir) {
	    this.templateDir = templateDir;
	  }

	  /** Set the resource file suffix, .java or .xxx */
	  public void setSuffix(String suffix) {
	    this.suffix = suffix;
	  }
	  
	  /**
	   * @return true if the record fields should be marked as deprecated
	   */
	  public boolean deprecatedFields() {
	    return (this.fieldVisibility == FieldVisibility.PUBLIC_DEPRECATED);
	  }

	  /**
	   * @return true if the record fields should be public
	   */
	  public boolean publicFields() {
	    return (this.fieldVisibility == FieldVisibility.PUBLIC
	        || this.fieldVisibility == FieldVisibility.PUBLIC_DEPRECATED);
	  }

	  /**
	   * @return true if the record fields should be private
	   */
	  public boolean privateFields() {
	    return (this.fieldVisibility == FieldVisibility.PRIVATE);
	  }

	  /**
	   * Sets the field visibility option.
	   */
	  public void setFieldVisibility(FieldVisibility fieldVisibility) {
	    this.fieldVisibility = fieldVisibility;
	  }

	  public boolean isCreateSetters() {
	    return this.createSetters;
	  }

	  /**
	   * Set to false to not create setter methods for the fields of the record.
	   */
	  public void setCreateSetters(boolean createSetters) {
	    this.createSetters = createSetters;
	  }

	  public boolean isCreateOptionalGetters() {
	    return this.createOptionalGetters;
	  }

	  /**
	   * Set to false to not create the getters that return an Optional.
	   */
	  public void setCreateOptionalGetters(boolean createOptionalGetters) {
	    this.createOptionalGetters = createOptionalGetters;
	  }

	  public boolean isGettersReturnOptional() {
	    return this.gettersReturnOptional;
	  }

	  /**
	   * Set to false to not create the getters that return an Optional.
	   */
	  public void setGettersReturnOptional(boolean gettersReturnOptional) {
	    this.gettersReturnOptional = gettersReturnOptional;
	  }
	  
	  private void enqueue(CobolSchema schema) {
		  //TODO finish build queueing
	  }
}
