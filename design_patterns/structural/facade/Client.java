package design_patterns.structural.facade;

import design_patterns.structural.facade.email.Email;
import design_patterns.structural.facade.email.EmailFacade;
import design_patterns.structural.facade.email.Mailer;
import design_patterns.structural.facade.email.Stationary;
import design_patterns.structural.facade.email.StationaryFactory;
import design_patterns.structural.facade.email.Template;
import design_patterns.structural.facade.email.Template.TemplateType;
import design_patterns.structural.facade.email.TemplateFactory;

public class Client {

	public static void main(String[] args) {
		Order order = new Order("101", 99.99);

		EmailFacade facade = new EmailFacade();
		boolean result = facade.sendOrderEmail(order);

		System.out.println("Order Email " + (result?"sent!":"NOT sent..."));

		boolean resultWithoutFacade = sendOrderEmailWithoutFacade(order);
		System.out.println("Order Email without facade " + (resultWithoutFacade?"sent!":"NOT sent..."));
	}

	private static boolean sendOrderEmailWithoutFacade(Order order) {
		Template template = TemplateFactory.createTemplateFor(TemplateType.Email);
		Stationary stationary = StationaryFactory.createStationary();
		Email email = Email.getBuilder()
					  .withTemplate(template)
					  .withStationary(stationary)
					  .forObject(order)
					  .build();
		Mailer mailer = Mailer.getMailer();
		return mailer.send(email);
	}

}
