package codebrew.doctorgeondam.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ChatDto {
  @Getter
  @Setter
  public static class ChatRequest {
    private String message;
  }

  @Getter
  @AllArgsConstructor
  public static class ChatResponse {
    private String reply;
  }
}
