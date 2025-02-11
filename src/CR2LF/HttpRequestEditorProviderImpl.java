package CR2LF;

import java.awt.Component;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.RawEditor;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpRequestEditor;
import burp.api.montoya.ui.editor.extension.HttpRequestEditorProvider;


public class HttpRequestEditorProviderImpl implements HttpRequestEditorProvider {
    MontoyaApi api;

    public HttpRequestEditorProviderImpl(MontoyaApi api) {
        this.api = api;
    }

    @Override
    public ExtensionProvidedHttpRequestEditor provideHttpRequestEditor(EditorCreationContext creationContext) {
        return new CR2LFRequestEditor(api, creationContext);
    }

    private static class CR2LFRequestEditor implements ExtensionProvidedHttpRequestEditor {        
        private HttpRequestResponse currentHttpRequestResponse;
        private RawEditor requestEditorTab;

        public CR2LFRequestEditor(MontoyaApi api, EditorCreationContext creationContext) {            
            requestEditorTab = api.userInterface().createRawEditor(EditorOptions.WRAP_LINES);
        }

        @Override
        public HttpRequest getRequest() {
            if (isModified()){
                ByteArray modifiedRequest = requestEditorTab.getContents();
                return HttpRequest.httpRequest(modifiedRequest);
            }
            return currentHttpRequestResponse.request();
        }

        @Override
        public void setRequestResponse(HttpRequestResponse requestResponse) {
            currentHttpRequestResponse = requestResponse;
            ByteArray request = ByteArray.byteArray(currentHttpRequestResponse.request().toString().replaceAll("\r\n", "\n"));
            requestEditorTab.setContents(request);      
        }

        @Override
        public boolean isEnabledFor(HttpRequestResponse requestResponse) {
            return true;
        }

        @Override
        public String caption() {
            return "CR2LF";
        }

        @Override
        public Component uiComponent() {
            return requestEditorTab.uiComponent();
        }

        @Override
        public Selection selectedData() {
            return requestEditorTab.selection().isPresent() ? requestEditorTab.selection().get() : null;
        }

        @Override
        public boolean isModified() {
            return requestEditorTab.isModified();
        }
        
    }
}
    

