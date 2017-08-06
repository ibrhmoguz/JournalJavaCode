package com.crossover.trial.journals.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * The Class JournalMessageBuilder.
 */
@Service
public class JournalMessageBuilder implements MessageBuilder{

	private static final String NEW_JOURNAL_TEMPLATE = "newJournalTemplate";
	
	/** The template engine. */
	private TemplateEngine templateEngine;
	
	/**
	 * Instantiates a new journal message builder.
	 *
	 * @param templateEngine the template engine
	 */
	@Autowired
    public JournalMessageBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
	
	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.notify.MessageBuilder#build(java.lang.String)
	 */
	@Override
	public String build(String message) {
		Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process(NEW_JOURNAL_TEMPLATE, context);
	}

}
