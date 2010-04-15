package org.xbrlapi.xlink;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.Exception;

public class XLinkException extends Exception 
{
	/**
     * 
     */
    private static final long serialVersionUID = 6594154185788570002L;
    private Throwable cause;
	
    public XLinkException() {
    	super();
    }

    public XLinkException(Throwable e) {
    	super();
    	cause = e;
    }

    public XLinkException(String reason) {
    	super("XLink Exception: " + reason);
    }

    public XLinkException(String reason, Exception e) {
    	super(" XLink Exception: " + reason);
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
            s = s + "\nCaused by: " + getCause() + " " + getCause().getMessage();
        }
        return s;
    }
    
}
