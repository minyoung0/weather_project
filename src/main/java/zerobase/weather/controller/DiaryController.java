package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.Service.DiaryService;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@RestController
//401,500 코드
public class DiaryController {


    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }
    @ApiOperation(value="일기 텍스트와 날씨를 이용해서 DB에 일기저장")//value=swagger에서 컨트롤러 한줄 설명,notes= 눌렀을때 뜨는 설명
    @PostMapping("/create/diary")
    public void  createDiary(@RequestParam("date") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date
                      ,@RequestBody  String text){
        diaryService.createDiary(date,text);
    }

    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diary")
    public List<Diary> readDiary(@RequestParam("date") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        return diaryService.readDiary(date);

    }
    @ApiOperation("선택한 기간 중의 모든 일기데이터를 가져옵니다")
    @GetMapping("/read/diaries")
    public List<Diary> readDiaries(@RequestParam("startDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)  @ApiParam(value="조회할 기간의 시작 날",example="2020-02-02") LocalDate startDate,
                            @RequestParam("endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE ) @ApiParam(value="조회할 기간의 마지막날",example="2020-02-02") LocalDate endDate){
        //@ApiParm -> 변수에 대한 설명 추가 기재
        return diaryService.readDiaries(startDate,endDate);
    }

    @PutMapping("/update/diary")
    public void updateDiary(@RequestParam("date")@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate date,
                     @RequestBody String text){
        diaryService.updateDiary(date,text);
    }

    @DeleteMapping("/delete/diary")
    public void deleteDiary(@RequestParam("date")@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate date){
        diaryService.deleteDiary(date);
    }

}
