import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hui.quick.plugin.base.file.FileReadUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YiHui
 * @date 2024/7/12
 */
public class VoiceGen {

    public static void main(String[] args) throws IOException {
//        loadVoice();
        loadLanTrans();
    }

    private static void loadVoice() throws IOException {
        Map<String, String> voiceMap = loadLanTrans();
        String content = FileReadUtil.readAll("voicelist.json");

        JSONArray ary = JSONArray.parseArray(content);
        for (Object obj : ary) {
            JSONObject item = (JSONObject) obj;
            String shortName = item.getString("ShortName");
            String gender = item.getString("Gender").equalsIgnoreCase("Male") ? "男" : "女";
            String locale = item.getString("Locale");
            JSONObject voiceTag = item.getJSONObject("VoiceTag");

            String key = shortName.replaceAll("-", "_");
            System.out.println("// " + voiceMap.get(locale) + " --> " + voiceTag.toJSONString());
            System.out.println(key + "(\"" + shortName + "\", \"" + gender + "\", \"" + locale + "\"),");
        }
    }

    private static Map<String, String> loadLanTrans() throws IOException {
        String content = FileReadUtil.readAll("voiceLan.txt");
        Map<String, String> map = new HashMap<>();
        for (String line : content.split("\n")) {
            String[] cells = line.split(" ");
            if (cells.length == 1) {
                cells = line.split("\t");
            }

            String key = null;
            String val = null;
            for (String cell : cells) {
                cell = cell.trim();
                if (cell.length() > 0) {
                    if (key == null) {
                        key = cell;
                    } else if (val == null) {
                        val = cell;
                        break;
                    }
                }
            }
            map.put(key, val);
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String e = entry.getKey().replaceAll("-", "_");
            String val = entry.getValue();
            String[] cells = val.split("\\(");
            String lan = null, area = null;
            if (cells.length == 2) {
                lan = cells[0].trim();
                area = cells[1].replace(")", "").trim();
            } else {
                lan = val;
                area = val;
            }

            System.out.println(e + "(\"" + entry.getKey() + "\", \"" + lan + "\", \"" + area + "\"),");
        }
        return map;
    }
}
