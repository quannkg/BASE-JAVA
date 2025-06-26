package webapi.application.service.selenium;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.stereotype.Service;
import webapi.application.service.selenium.interfaces.IGeminiSentimentAnalyzer;
import webapi.domain.ApiIntegrated;
import webapi.domain.FacebookComment;
import webapi.infrastructure.common.Constants;
import webapi.infrastructure.exception.AppException;
import webapi.infrastructure.repositories.ApiIntegratedRepository;

@Service
public class GeminiSentimentAnalyzerService implements IGeminiSentimentAnalyzer {
    private final ApiIntegratedRepository apiIntegratedRepository;

    public GeminiSentimentAnalyzerService(ApiIntegratedRepository apiIntegratedRepository) {
        this.apiIntegratedRepository = apiIntegratedRepository;
    }

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void analyzeCommentFacebookSentiment(FacebookComment comment) {
        try {
            ApiIntegrated apiIntegrated = apiIntegratedRepository.findFirstByCodeAndIsDeleted(Constants.APIKeyIntegrated.GEMINI, Boolean.FALSE);
            if (apiIntegrated == null) {
                throw new AppException("Gemini API is not active or not configured.");
            }

            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiIntegrated.getApiKey();

            String prompt = "Phân tích bình luận sau và trả lời chỉ một từ là nhãn kết quả: Tích cực, Tiêu cực, hoặc Trung lập. "
                    + "Không giải thích thêm.\n\nBình luận: \"" + comment.getCommentText() + "\"\nTrả lời:";

            ObjectNode part = mapper.createObjectNode();
            part.put("text", prompt);

            ObjectNode content = mapper.createObjectNode();
            content.put("role", "user");
            content.putArray("parts").add(part);

            ObjectNode root = mapper.createObjectNode();
            root.putArray("contents").add(content);

            RequestBody body = RequestBody.create(
                    mapper.writeValueAsString(root),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new AppException("Gemini API Error: " + response);
                }

                String responseBody = response.body().string();
                String result = mapper.readTree(responseBody)
                        .at("/candidates/0/content/parts/0/text")
                        .asText()
                        .toLowerCase();

                System.out.println("📊 Sentiment: " + result + " 🔍 Comment: " + comment.getCommentText());

                comment.setPositive(result.contains("tích cực"));
                comment.setNegative(result.contains("tiêu cực"));
                comment.setNeutral(result.contains("trung lập"));
            }
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi khi gọi Gemini API: " + e.getMessage());
        }
    }
}
