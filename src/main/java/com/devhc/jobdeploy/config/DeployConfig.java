package com.devhc.jobdeploy.config;

import com.devhc.jobdeploy.utils.ConfigFile;
import org.springframework.stereotype.Component;

@Component
public class DeployConfig extends ConfigFile {
  private static final long serialVersionUID = 4822043442950861585L;

  public String getAzkabanUrl() {
    return getProperty("azkaban.url");
  }
}
