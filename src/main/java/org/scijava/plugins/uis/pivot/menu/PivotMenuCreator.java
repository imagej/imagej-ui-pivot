/*
 * #%L
 * SciJava UI components for Apache Pivot.
 * %%
 * Copyright (C) 2011 - 2014 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.plugins.uis.pivot.menu;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.Menu.SectionSequence;
import org.apache.pivot.wtk.MenuButton;
import org.apache.pivot.wtk.PushButton;
import org.scijava.menu.AbstractMenuCreator;
import org.scijava.menu.ShadowMenu;
import org.scijava.module.ModuleInfo;

/**
 * Populates a {@link BoxPane} with menu items from a {@link ShadowMenu}.
 * 
 * @author Curtis Rueden
 */
public class PivotMenuCreator extends AbstractMenuCreator<BoxPane, MenuButton> {

	// -- Internal methods --

	@Override
	protected void
		addLeafToMenu(final ShadowMenu shadow, final MenuButton target)
	{
		final Menu.Item item = new Menu.Item(shadow.getMenuEntry().getName());
		linkAction(shadow, item);
		getLastSection(target).add(item);
	}

	@Override
	protected void addLeafToTop(final ShadowMenu shadow, final BoxPane target) {
		final PushButton button = new PushButton();
		button.setButtonData(shadow.getMenuEntry().getName());
		linkAction(shadow, button);
		target.add(button);
	}

	@Override
	protected MenuButton addNonLeafToMenu(final ShadowMenu shadow,
		final MenuButton target)
	{
		final MenuButton button = createMenuButton(shadow);
		final Menu.Item item = new Menu.Item(shadow.getMenuEntry().getName());
		getLastSection(target).add(item);
		return button;
	}

	@Override
	protected MenuButton addNonLeafToTop(final ShadowMenu shadow,
		final BoxPane target)
	{
		final MenuButton button = createMenuButton(shadow);
		target.add(button);
		return button;
	}

	@Override
	protected void addSeparatorToMenu(final MenuButton target) {
		target.getMenu().getSections().add(new Menu.Section());
	}

	@Override
	protected void addSeparatorToTop(final BoxPane target) {
		target.add(new Label("|"));
	}

	// -- Helper methods --

	private MenuButton createMenuButton(final ShadowMenu shadow) {
		final MenuButton button = new MenuButton();
		button.setButtonData(shadow.getMenuEntry().getName());
		final Menu menu = new Menu();
		button.setMenu(menu);
		menu.getSections().add(new Menu.Section());
		assignProperties(shadow, button);
		return button;
	}

	private Menu.Section getLastSection(final MenuButton target) {
		final SectionSequence sections = target.getMenu().getSections();
		return sections.get(sections.getLength() - 1);
	}

	private void
		assignProperties(final ShadowMenu shadow, final MenuButton button)
	{
		final ModuleInfo info = shadow.getModuleInfo();
		if (info != null) button.setEnabled(info.isEnabled());
	}

	private void linkAction(final ShadowMenu shadow, final Button button) {
		button.setAction(new Action() {

			@Override
			public void perform(final Component c) {
				shadow.run();
			}
		});
	}

}
