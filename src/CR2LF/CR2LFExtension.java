package CR2LF;
import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class CR2LFExtension implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("CR2LF");
        api.userInterface().registerHttpRequestEditorProvider(new HttpRequestEditorProviderImpl(api));
        api.userInterface().registerHttpResponseEditorProvider(new HttpResponseEditorProviderImpl(api));
        api.logging().logToOutput("CR2LF Extension Loaded");
    }
}