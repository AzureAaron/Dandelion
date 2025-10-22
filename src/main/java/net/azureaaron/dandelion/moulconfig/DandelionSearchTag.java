package net.azureaaron.dandelion.moulconfig;

import java.lang.annotation.Annotation;

import io.github.notenoughupdates.moulconfig.annotations.SearchTag;

//Silence the compiler over implementing the annotation
@SuppressWarnings("all")
public class DandelionSearchTag implements SearchTag {
	private final String tag;

	protected DandelionSearchTag(String tag) {
		this.tag = tag;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return SearchTag.class;
	}

	@Override
	public String toString() {
		return this.tag;
	}

	@Override
	public String value() {
		return this.tag;
	}
}
