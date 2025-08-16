package zerobase.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //spring에서 전역예외 처리기를 선언할 때 사용하는 어노테이션 (@ControllerAdvice + @ResponseBody)
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class) //지정한 타입(Exception)의 예외가 발생했을 때 해당 메서드가 실행됨 . 여기서는 Exception.class( 즉, 모든 예외(Exception 최상위)를 찹음)
    //특정 예외만 잡고싶으면 (@ExceptionHandelr(NullPointerException.class) 이런식으로 사용 가능)
    public Exception handleAllException(){
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }
}
