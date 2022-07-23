package com.miBudget.utilities;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonUtility {
    private static final Logger LOGGER = LogManager.getLogger(JsonUtility.class);

    /**
     * Convert src to json string
     * @param src
     * @return
     */
    public String changeToJsonString(Object src) {
        Gson gson = new Gson();
        String srcAsJson = gson.toJson(src);
        LOGGER.info("Object as Json String");
        LOGGER.info(srcAsJson);
        if (srcAsJson.equals("{}")) {
            return "";
        }
        return srcAsJson;
    }
}
