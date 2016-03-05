package fr.scale.gcm_scalable.controller.components.wrapone.nf.interceptor;
 
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.component.control.AbstractPAController;
import org.objectweb.proactive.core.component.interception.Interceptor;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactoryImpl;
import org.objectweb.proactive.core.mop.MethodCall;


public class InterceptorIn extends AbstractPAController implements Interceptor, ControllerItf {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InterceptorIn(Component owner) {
        super(owner);
    }

    protected void setControllerItfType() {
        try {
            setItfType(PAGCMTypeFactoryImpl.instance().createFcItfType("myinterceptor-controller",
                    ControllerItf.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY,
                    TypeFactory.SINGLE));
        } catch (InstantiationException e) {
            throw new ProActiveRuntimeException("cannot create controller" + this.getClass().getName());
        }
    }

    // foo is defined in the MyController interface
    public void foo() {
        // foo implementation
    }

    public void beforeMethodInvocation(String interfaceName, MethodCall methodCall) {
        System.out.println("pre processing an intercepted a functional invocation");
        // interception code
    }

    public void afterMethodInvocation(String interfaceName, MethodCall methodCall, Object result) {
        System.out.println("post processing an intercepted a functional invocation");
        // interception code
    }
}
//@snippet-end component_userguide_14

