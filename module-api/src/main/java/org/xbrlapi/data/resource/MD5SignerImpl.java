package org.xbrlapi.data.resource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.log4j.Logger;
import org.xbrlapi.utilities.XBRLException;

/**
 * @author Geoffrey Shuetrim (geoff@galexy.net)
 */

public class MD5SignerImpl implements Signer {

    Logger logger = Logger.getLogger(MD5SignerImpl.class);    

    public MD5SignerImpl() {
        super();
    }
    
    public String getSignature(List<String> lines) throws XBRLException {
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
    
    private String MD5(String content) throws XBRLException {
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
            throw new XBRLException("There is no available MD5 algorithm.",e);
        }
    }

}