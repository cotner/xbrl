package org.xbrlapi.utilities;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.io.PrintStream;
import java.io.PrintWriter;

public class XBRLException extends Exception 
{
	
	/**
     * 
     */
    private static final long serialVersionUID = 9071731735229812074L;
    private Throwable cause;
	
    public XBRLException() {
    	super();
    }

    public XBRLException(Throwable e) {
    	super();
    	cause = e;
    }

    public XBRLException(String reason) {
    	super("XBRLAPI Exception: " + reason);
    }

    public XBRLException(String reason, Throwable e) {
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
