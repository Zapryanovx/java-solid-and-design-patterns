package design_patterns.creational.singleton.lazy.iodh;

public class Client {

	public static void main(String[] args) {
		LazyRegistryIODH singleton;
		singleton = LazyRegistryIODH.getInstance();
		singleton = LazyRegistryIODH.getInstance();
		singleton = LazyRegistryIODH.getInstance();

	}

}
