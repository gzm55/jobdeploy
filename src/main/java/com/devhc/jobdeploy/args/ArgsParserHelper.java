package com.devhc.jobdeploy.args;

import com.google.common.collect.Lists;
import com.devhc.jobdeploy.config.Constants;
import com.devhc.jobdeploy.exception.DeployException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ArgsParserHelper {
  private static Logger log = LoggerFactory.getLogger(ArgsParserHelper.class);

  /**
   * 解析参数
   * @param args
   * @return
   */
  public static AppArgs parseAppArgs(String[] args) {
    AppArgs appArgs = new AppArgs();
    if (args.length == 0) {
      return appArgs;
    }
    boolean headOptionsFinished = false;
    for (int i = 0; i < args.length; i++) {
      String currentArg = args[i];
      if (currentArg.startsWith("-") || headOptionsFinished) {
        if (!headOptionsFinished) {
          appArgs.getHeadOptions().add(currentArg);
        } else {
          appArgs.getTaskOptions().add(currentArg);
        }
      } else {
        String stageTask = currentArg;
        String stageTaskArr[] = stageTask.split(":");
        switch (stageTaskArr.length) {
        case 1:
          appArgs.setStage(stageTaskArr[0]);
          break;
        case 2:
          appArgs.setStage(stageTaskArr[0]);
          appArgs.setTask(stageTaskArr[1]);
          break;
        default:
          throw new DeployException("not support taskDescrition >2");
        }
        headOptionsFinished = true;
      }
    }

    return appArgs;
  }

  public static AppArgs parseAgs(String[] args) {
    AppArgs appArgs = new AppArgs();
    List<String> headCmdOptions = Lists.newArrayList();
    String stage = "";
    if (args.length > 0 && !args[0].startsWith("-")) {
      stage = args[0];
      for (int i = 0; i < args.length; i++) {
        String v = args[i];
        if (!v.startsWith("-")) {
          headCmdOptions.add(v);
        } else {
          break;
        }
      }

      List<String> argsList = Arrays.asList(args);
      argsList = argsList.subList(headCmdOptions.size(), argsList.size());
      appArgs.setTaskOptions(argsList);
      args = argsList.toArray(new String[argsList.size()]);
    }

    appArgs.setStage(stage);
    String task = Constants.DEFAULT_TASK;
    switch (headCmdOptions.size()) {
    case 0:
      break;
    case 1:
      String val = headCmdOptions.get(0);
      String info[] = val.split(":", 2);
      if (info.length == 1) {
        stage = info[0];
      } else if (info.length == 2) {
        stage = info[0];
        if (StringUtils.isNotEmpty(info[1])) {
          task = info[1];
        }
      } else {
      }
      break;
    default:
      throw new DeployException("unsupport head no - option than 2");
    }
    log.info("task:{} stage:{}", task, stage);
    appArgs.setTask(task);
    return appArgs;
  }
}
