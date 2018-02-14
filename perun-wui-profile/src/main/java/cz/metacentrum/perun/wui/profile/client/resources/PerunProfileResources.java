package cz.metacentrum.perun.wui.profile.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Perun Profile app resources
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunProfileResources extends ClientBundle {

	PerunProfileResources INSTANCE = GWT.create(PerunProfileResources.class);

	interface PerunProfileCss extends CssResource {

		String webContent();

		String pageWrapper();

		String page();

		String grid();

		String smallGrid();

		String logoWrapper();

		String identities();

		String personalInfoLabel();

		String mobileContainer();

		String perunBreadcrumb();

		String black();

		String settings();

		String sshKeysTable();
	}

	@Source("css/PerunProfile.gss")
	PerunProfileCss gss();

}
