package webapi.application.service.selenium.interfaces;

import webapi.domain.FacebookComment;

public interface IGeminiSentimentAnalyzer {
    void analyzeCommentFacebookSentiment(FacebookComment comment);
}
