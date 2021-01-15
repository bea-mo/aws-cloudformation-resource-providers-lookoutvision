package software.amazon.lookoutvision.project;

import software.amazon.awssdk.services.lookoutvision.model.ResourceNotFoundException;
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;

import java.util.Objects;

public class DeleteHandler extends BaseHandler<CallbackContext> {
    private Logger logger;

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
        final AmazonWebServicesClientProxy proxy,
        final ResourceHandlerRequest<ResourceModel> request,
        final CallbackContext callbackContext,
        final Logger logger) {

        this.logger = logger;

        final ResourceModel model = request.getDesiredResourceState();
        try {
            proxy.injectCredentialsAndInvokeV2(Translator.translateToDeleteRequest(model),
                ClientBuilder.getClient()::deleteProject);
        } catch (ResourceNotFoundException e) {
            throw new software.amazon.cloudformation.exceptions.ResourceNotFoundException(ResourceModel.TYPE_NAME,
                Objects.toString(model.getPrimaryIdentifier()));
        }

        final String message = String.format("%s successfully deleted.", ResourceModel.TYPE_NAME);
        logger.log(message);
        return ProgressEvent.defaultSuccessHandler(null);
    }
}