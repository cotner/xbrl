package org.xbrlapi.xmlbase;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.Exception;

public class XMLBaseException extends Exception 
{
	
	/**
     * 
     */
    private static final long serialVersionUID = -6062388656987869506L;
    private Throwable cause;
	
    public XMLBaseException() {
    	super();
    }

    public XMLBaseException(Throwable e) {
    	super();
    	cause = e;
    }

    public XMLBaseException(String reason) {
    	super("XBRLAPI Exception: " + reason);
    }

    public XMLBaseException(String reason, Exception e) {
    	super(" XBRLAPI Exception: " + reason);
    	cause = e;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
        if (getCause() != null) {
            System.err.print("Caused by: ");
            this.cause.printStackTrace(System.err);
        }
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (getCause() != null) {
            s.print("Caused by: ");
            this.cause.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (getCause() != null) {
            s.print("Caused by: ");
            this.cause.printStackTrace(s);
        }
    }

    public Throwable getCause() {
        return cause;
    }
    
    public String getMessage() {
    	String s = super.getMessage();
        if (getCause() != null) {
            s = s + "\nCaused by: " + getCause().getMessage();
        }
        return s;
    }
    
}
