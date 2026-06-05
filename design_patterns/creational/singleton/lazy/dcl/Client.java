package design_patterns.creational.singleton.lazy.dcl;

public class Client {

	public static void main(String[] args) {

		LazyRegistryWithDCL registryWithDCL = LazyRegistryWithDCL.getInstance();
		LazyRegistryWithDCL registryWithDCL2 = LazyRegistryWithDCL.getInstance();
		System.out.println(registryWithDCL == registryWithDCL2);
	}

}
