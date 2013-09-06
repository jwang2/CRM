package com.autopay.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author sadimelbouci
 */
public class UtilsProps {
    
    public static Properties load (String fileName) throws IOException {
        Properties props = new Properties();
        InputStream is = UtilsProps.class.getClassLoader().getResourceAsStream(fileName);
        
        props.load(is);
        
        return props;        
    }
    

}
