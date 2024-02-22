package paulevs.graphene.rendering.disposing;

import java.util.ArrayList;
import java.util.List;

public class DisposeUtil {
	private static final List<Disposable> OBJECTS = new ArrayList<>(256);
	
	public static void add(Disposable object) {
		OBJECTS.add(object);
	}
	
	public static void disposeAll() {
		OBJECTS.forEach(Disposable::dispose);
	}
}
