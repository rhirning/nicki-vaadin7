
package org.mgnl.nicki.vaadin.base.components;

/*-
 * #%L
 * nicki-vaadin-base
 * %%
 * Copyright (C) 2017 Ralf Hirning
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mgnl.nicki.core.thread.NickiProgress;
import org.mgnl.nicki.core.thread.ProgressRunner;
import org.mgnl.nicki.vaadin.base.notification.Notification;
import org.mgnl.nicki.vaadin.base.notification.Notification.Type;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

@SuppressWarnings("serial")
public class NickiProgressComponent extends VerticalLayout implements NickiProgress {
	
	private Label details;
	
	private Label progressLabel;
	
	private ProgressBar progress;
	
	private Label title;

	private int count;
	private int current;
	
	private List<FinishedListener> finishedListeners;


	public void init(ProgressRunner runner) {
		buildMainLayout();
		count = runner.getCount();
		title.setText(runner.getTitle());
		progressed(0, "");
        UI.getCurrent().setPollInterval(500);

	}

	public void progressed(int newCurrent, String newDetails) {
		this.current = newCurrent;
		float value = new Float(current) / count;
		progress.setValue(value);
		details.setText(newDetails);
		if (StringUtils.isNotBlank(newDetails)) {
			Notification.show(newDetails, Type.TRAY_NOTIFICATION);
		}
		if (current < count)
			progressLabel.setText("" + ((int) (value * 100)) + "% ("+ current + "/" + count + ")");
		else
			progressLabel.setText("finished");
	}

	
	private void buildMainLayout() {
		setWidth("100%");
		setHeight("-1px");
		setMargin(true);
		setSpacing(true);

		// title
		title = new Label();
		title.setWidth("-1px");
		title.setHeight("-1px");
		title.setText("title");
		add(title);

		// progress
		progress = new ProgressBar();
		progress.setWidth("-1px");
		progress.setHeight("-1px");
		add(progress);

		// progressLabel
		progressLabel = new Label();
		progressLabel.setWidth("-1px");
		progressLabel.setHeight("-1px");
		progressLabel.setText("Label");
		add(progressLabel);

		// details
		details = new Label();
		details.setWidth("-1px");
		details.setHeight("-1px");
		details.setText("Details");
		add(details);
	}

	public void finish() {
        // Update the UI thread-safely
        UI.getCurrent().access(() -> {
                // Stop polling
                UI.getCurrent().setPollInterval(-1);
                setVisible(false);
        });
        if (finishedListeners != null) {
        	for (FinishedListener finishedListener : finishedListeners) {
				finishedListener.finished();
			}
        }
	}

	public void addFinishedListener(FinishedListener finishedListener) {
		if (this.finishedListeners == null) {
			this.finishedListeners = new ArrayList<>();
		}
		this.finishedListeners.add(finishedListener);
	}

}
