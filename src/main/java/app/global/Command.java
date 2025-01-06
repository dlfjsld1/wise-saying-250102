package app.global;

import java.util.HashMap;
import java.util.Map;

public class Command {
    //1. 액션 네임 제공
    //2. id 파라미터 제공
    String actionName;
    Map<String, String> paramMap;
    public Command(String cmd) {
        paramMap = new HashMap<>();
        String[] cmdBits = cmd.split("\\?"); //[삭제, id=1]
        actionName = cmdBits[0];

        if(cmdBits.length < 2) {
            return;
        }

        //배열 -> ArrayList
        //맵 -> Map
        //key1=val1&key2=val2
        String queryString = cmdBits[1];

        String[] params = queryString.split("&");
        for(String param : params) {
            String[] paramBits = param.split("=", 2);

            if(paramBits.length < 2) {
                continue;
            }
            paramMap.put(paramBits[0], paramBits[1]);
        }
        //목록?expr=1=1
        //목록?   key1=val1    &   key?=val2
        //split의 두 번째 매개변수는 몇 개까지 쪼개냐임

    }
    public String getActionName() {
        return actionName;
    }

    public String getParam(String key) {

        return paramMap.get(key);
    }

    public int getParamAsInt(String key) {
        try {
            String param = paramMap.get(key);
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
