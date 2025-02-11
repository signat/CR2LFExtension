package CR2LF;

import java.awt.Component;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.RawEditor;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import burp.api.montoya.ui.editor.extension.HttpResponseEditorProvider;

public class HttpResponseEditorProviderImpl implements HttpResponseEditorProvider {
    MontoyaApi api;

    public HttpResponseEditorProviderImpl(MontoyaApi api) {
        this.api = api;
    }

    @Override
    public ExtensionProvidedHttpResponseEditor provideHttpResponseEditor(EditorCreationContext creationContext) {
        return new CR2LFResponseEditor(api, creationContext);
    }

    private class CR2LFResponseEditor implements ExtensionProvidedHttpResponseEditor {
        HttpRequestResponse currentHttpRequestResponse;
        RawEditor responseEditorTab;

        public CR2LFResponseEditor(MontoyaApi api, EditorCreationContext creationContext){
            responseEditorTab = api.userInterface().createRawEditor(EditorOptions.WRAP_LINES);
        }

        @Override
        public HttpResponse getResponse() {
            if (isModified()){
                ByteArray modifiedResponse = responseEditorTab.getContents();
                return HttpResponse.httpResponse(modifiedResponse);
            }
            return currentHttpRequestResponse.response();
        }

        @Override
        public void setRequestResponse(HttpRequestResponse requestResponse) {
            currentHttpRequestResponse = requestResponse;
            ByteArray response = ByteArray.byteArray(currentHttpRequestResponse.response().toString().replaceAll("\r\n", "\n"));
            responseEditorTab.setContents(response);
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
            return responseEditorTab.uiComponent();
        }

        @Override
        public Selection selectedData() {
            return responseEditorTab.selection().isPresent() ? responseEditorTab.selection().get() : null;
        }

        @Override
        public boolean isModified() {
            return responseEditorTab.isModified();
        }
        
    }
    
}
