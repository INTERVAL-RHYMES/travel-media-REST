package com.example.travelmediarest.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class MyCustomErrorAttributes extends DefaultErrorAttributes {

//    @Override
//    public Map<String, Object> getErrorAttributes(
//            WebRequest webRequest, ErrorAttributeOptions options) {
//        Map<String, Object> errorAttributes =
//                super.getErrorAttributes(webRequest, options);
//        errorAttributes.put("locale", webRequest.getLocale()
//                .toString());
//        errorAttributes.remove("error");
//
//        //...
//
//        return errorAttributes;
//    }
}

//***********************************
// ExceptionHandler -> only active for that particular controller

//        @ExceptionHandler({ CustomException1.class, CustomException2.class })
//        public void handleException() {
//            //
//        }
//************************************
//HandlerExceptionResolver -> resolve any exception thrown by the application


//ResponseStatusException

/*

@ControllerAdvice
public class RestResponseEntityExceptionHandler
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(
      RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse,
          new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}


*ResponseStatusException
*
 @GetMapping(value = "/{id}")
 public Foo findById(@PathVariable("id") Long id, HttpServletResponse response) {
    try {
        Foo resourceById = RestPreconditions.checkFound(service.findOne(id));

        eventPublisher.publishEvent(new SingleResourceRetrievedEvent(this, response));
        return resourceById;
    }
    catch (MyResourceNotFoundException exc) {
         throw new ResponseStatusException(
           HttpStatus.NOT_FOUND, "Foo Not Found", exc);
    }
 }


 @PutMapping("/actor/{id}/{name}")
 public String updateActorName( @PathVariable("id") int id, @PathVariable("name") String name) {

    try {
        return actorService.updateActor(id, name);
    } catch (ActorNotFoundException ex) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Provide correct Actor Id", ex);
    }
}
*
*
* */
