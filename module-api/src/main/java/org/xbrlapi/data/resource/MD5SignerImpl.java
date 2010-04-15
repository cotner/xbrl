package org.xbrlapi.data.resource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MD5SignerImpl implements Signer {

    /**
     * 
     */
    private static final long serialVersionUID = -4391859287548042192L;

    private static final Logger logger = Logger.getLogger(MD5SignerImpl.class);    

    public MD5SignerImpl() {
        super();
    }
    
    public String getSignature(List<String> lines) {
        String document = "";
        double divisor = Math.ceil(lines.size() / 21);
        for (int i=0; i<lines.size(); i++) {
            if (i < 40) {
                document = document + lines.get(i);
            } else if (i > (lines.size()-10)) {
                document = document + lines.get(i);
            } else {
                double result = i/divisor;
                if ( result == Math.floor(result)) {
                    document = document + lines.get(i);
                }
            }
        }
        return (new Integer(lines.size())).toString() + MD5(document);
    }
    
    private String MD5(String content) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(content.getBytes());
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            
            for (int i=0;i<messageDigest.length;i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            logger.error("Your system is missing an MD5 algorithm.");
            return "corruptedSignature";
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }    
}
