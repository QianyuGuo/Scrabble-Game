package ScrabbleServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONException;
import org.json.JSONObject;


public class dataManager {


    public static final String path = "test.json";

    public JSONObject dataJson = null;

    /**
     * @param
     * @throws JSONException
     */

    public dataManager() throws JSONException {
        dataJson = read();
        createInitalFile();
    }

    public void createInitalFile() throws JSONException {
        String s = "";
        int i = 0;
        for (; i < 400; i++) {

            s = String.valueOf(i);
            dataJson.put(s, 1);
            write(dataJson);
        }
    }


    public JSONObject read() {
        StringBuilder builder = new StringBuilder();
        JSONObject dataJson = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    path));

            String s = null;
            if ((s = br.readLine()) != null) {
                builder.append(s);
            }

            br.close();

            dataJson = new JSONObject(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return dataJson;
    }

    public void write(JSONObject dataJson) {
        String ws = dataJson.toString();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(
                    path));

            bw.write(ws);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject getDataJson() {
        return dataJson;
    }

    public void setDataJson(JSONObject dataJson) {
        this.dataJson = dataJson;
    }
}  