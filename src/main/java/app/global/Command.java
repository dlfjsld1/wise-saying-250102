package app.global;

public class Command {
    //1. 액션 네임 제공
    //2. id 파라미터 제공
    String actionName;
    String paramKey;
    String paramValue;
    public Command(String cmd) {

        String[] cmdBits = cmd.split("\\?"); //[삭제, id=1]
        actionName = cmdBits[0];

        if(cmdBits.length < 2) {
            paramValue = "";
            return;
        }

        String param = cmdBits[1];
        String[] paramBits = param.split("=");
        String paramKey = paramBits[0];
        paramValue = paramBits[1];
    }
    public String getActionName() {
        return actionName;
    }

    public int getParam() {
        int id = Integer.parseInt(paramValue);
        return id;
    }
}
