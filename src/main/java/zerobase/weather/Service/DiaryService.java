package zerobase.weather.Service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;



    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Value("${openweathermap.key}") //application.properties에 있음
    //value로 분리하는 이유는 협업에서 test, 개발환경 등 다양한 db에 접근하기 위해서

    private String apiKey;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {

        //open weather map에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();
        System.out.println(getWeatherString());

        //받아온 날씨 json 파싱하기
        Map<String,Object> parsedWeather = parseWeather(weatherData);

        //파싱된 데이터 + 열기 값 우리 DB에 넣기
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parsedWeather.get("main").toString());
        nowDiary.setIcon(parsedWeather.get("icon").toString());
        Object t = parsedWeather.get("temp");
        double temp = (t instanceof Number) ? ((Number) t).doubleValue()
                : Double.parseDouble(String.valueOf(t));
        nowDiary.setTemperature(temp);
        nowDiary.setText(text);
        nowDiary.setDate(date);
        diaryRepository.save(nowDiary);
    }

    public List<Diary> readDiary(LocalDate date){
        return diaryRepository.findAllByDate(date);
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate){
        return diaryRepository.findAllByDateBetween(startDate,endDate);
    }

    public void updateDiary(LocalDate date, String text){
        Diary diary = diaryRepository.getFirstByDate(date);
        diary.setText(text);
        diaryRepository.save(diary);
    }

    public void deleteDiary(LocalDate date){
        diaryRepository.deleteAllByDate(date);
    }

    private String getWeatherString() {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=" + apiKey;
            URL url = new URL(apiUrl);

            //apiURL을 HTTP형식으로 호출하겠다
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //응답코드 (ex: 202,401,...)
            int code = connection.getResponseCode();

            BufferedReader br;
            if (code == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while (((inputLine = br.readLine()) != null)) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            return "failed to get response";
        }

    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
  /*      Map<String, Object> result = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        result.put("temp", mainData.get("temp"));
        JSONArray weatherArray=(JSONArray) jsonObject.get("weather");
        JSONObject weatherData=(JSONObject) weatherArray.get(0);
        result.put("main", mainData.get("main"));
        result.put("icon", mainData.get("icon"));
        return result;
*/
        Map<String, Object> resultMap = new HashMap<>();

        // main.temp
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));

        // weather[0].main / weather[0].icon
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather"); // ← JSONArray로 받아야 함
        if (weatherArray != null && !weatherArray.isEmpty()) {
            JSONObject weatherData = (JSONObject) weatherArray.get(0);
            resultMap.put("main", weatherData.get("main"));
            resultMap.put("icon", weatherData.get("icon"));
        }

        return resultMap;

    }
}
