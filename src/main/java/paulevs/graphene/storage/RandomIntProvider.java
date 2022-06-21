package paulevs.graphene.storage;

import paulevs.bhcore.util.MathUtil;

import java.util.Random;

public class RandomIntProvider {
	private final int minValue;
	private final int maxValue;
	
	public RandomIntProvider(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public int get(Random random) {
		return MathUtil.randomRange(minValue, maxValue, random);
	}
}
