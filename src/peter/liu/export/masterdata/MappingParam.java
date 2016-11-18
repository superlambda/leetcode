/**
 * 
 */
package com.wuerth.phoenix.basic.etnax.utilities.sap.export.masterdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.wuerth.phoenix.bcutil.PSystemException;
import com.wuerth.phoenix.bcutil.PhxLog;

/**
 * @author pcnsh222
 *
 */
public class MappingParam {
	private static MappingParam instance = new MappingParam();
	private String MAPPING_PATH = "../../etc/exportSAP/exportSAP.properties";
    private Properties propertiesForMapping = null;
	
    private MappingParam() {	
	}
    
    
	
    public static MappingParam getInstance() {
        return instance;
    }
    
	public Properties getMappingProperties() {
        if (propertiesForMapping == null) {
            Properties properties = new Properties();
            try {
                PhxLog.BUSINESS.message(PhxLog.INFORMATION, this, "getMappingProperties", "Reading SAP mapping "
                        + "properties from '" + MAPPING_PATH + "'.");
                properties.load(new FileInputStream(MAPPING_PATH));
                propertiesForMapping = properties;
            } catch (FileNotFoundException e) {
                PSystemException e2 = new PSystemException(e.getMessage());
                e2.setStackTrace(e.getStackTrace());
                throw e2;
            } catch (IOException e) {
                PSystemException e2 = new PSystemException(e.getMessage());
                e2.setStackTrace(e.getStackTrace());
                throw e2;
            }
            return properties;
        } else {
            return propertiesForMapping;
        }

    }
}
