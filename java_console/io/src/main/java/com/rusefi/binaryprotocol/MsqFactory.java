package com.gerefi.binaryprotocol;

import com.opensr5.ConfigurationImage;
import com.opensr5.ini.IniFileModel;
import com.gerefi.tune.xml.Msq;

public class MsqFactory {
    public static Msq valueOf(ConfigurationImage image, IniFileModel ini) {
        return Msq.valueOf(image, ini.getMetaInfo().getTotalSize(), ini.getSignature(), ini);
    }
}
