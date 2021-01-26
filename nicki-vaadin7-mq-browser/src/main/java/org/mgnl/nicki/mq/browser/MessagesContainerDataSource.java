package org.mgnl.nicki.mq.browser;

import java.util.Collection;

import javax.jms.Message;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.AbstractBeanContainer;


@SuppressWarnings("serial")
public class MessagesContainerDataSource extends AbstractBeanContainer<Message, Message> implements Container.Filterable {

	public MessagesContainerDataSource(Collection<? extends Message> collection)
            throws IllegalArgumentException {
        super(Message.class);
        super.setBeanIdResolver(new IdentityBeanIdResolver<Message>());
        
        add(collection);
    }

	private static class IdentityBeanIdResolver<BT> implements
            BeanIdResolver<BT, BT> {

        @Override
        public BT getIdForBean(BT bean) {
            return bean;
        }

    }

	public void add(Collection<? extends Message> collection) {
        if (collection != null) {
	        for (Message message : collection) {
				addBean(message);
			}
        }
	}
}
