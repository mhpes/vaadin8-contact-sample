package es.mhp.contacts;

import com.vaadin.event.dnd.DragSourceExtension;
import com.vaadin.event.dnd.DropTargetExtension;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import javax.annotation.PostConstruct;
import java.util.Random;

@UIScope
@SpringView(name = DragAndDropView.DRAG_AND_DROP_VIEW)
@SuppressWarnings("serial")
public class DragAndDropView extends HorizontalLayout implements View {
	public static final String DRAG_AND_DROP_VIEW = "DragAndDropView";

	private Panel container = new Panel();

	@PostConstruct
	public void postConstruct() {
		createLayout();
		container.setSizeFull();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		reorderingLayouts();
	}

	private void createLayout() {
		this.setSizeFull();

		Layout menu = createMenu();
		addComponents(menu, container);
	}

	private Layout createMenu() {
		VerticalLayout menu = new VerticalLayout();

		Button reorderingLayouts = createButton("Reordering layouts", e -> reorderingLayouts());
		Button reorderingFields = createButton("Reordering fields", e -> reorderingFields());
		Button reorderingFieldsLayouts = createButton("Reordering fields between layouts", e -> reorderingFieldsLayouts());

		menu.addComponents(reorderingLayouts, reorderingFields, reorderingFieldsLayouts);

		return menu;
	}

	private Button createButton(String caption, Button.ClickListener clickListener) {
		Button reorderingLayouts = new Button(caption);
		reorderingLayouts.addClickListener(clickListener);
		return reorderingLayouts;
	}

	private void reorderingLayouts() {
		VerticalLayout layouts = new VerticalLayout();
		for(int i = 0; i < new Random().nextInt((10) + 1) + 5; i++) {
			HorizontalLayout contentContainer = new HorizontalLayout(new Label(i + ""));
			contentContainer.setSizeFull();
			int rgb = (int) (Math.random()*(1<<24));
			String className = "s" + rgb;
			Page.getCurrent().getStyles().add("." + className + "{ background: #" + Integer.toHexString(rgb) + ";}");
			contentContainer.addStyleName(className);

			createDragSource(i, contentContainer);

			DropTargetExtension dropTarget = new DropTargetExtension<>(contentContainer);
			dropTarget.setDropEffect(DropEffect.MOVE);
			dropTarget.addDropListener(e -> {
				e.getDragSourceComponent().ifPresent(sourceComponent -> {
					Component targetComponent = dropTarget.getParent();
					int targetIndex = layouts.getComponentIndex(targetComponent);
					int sourceIndex = layouts.getComponentIndex((AbstractComponent)sourceComponent);

					layouts.addComponent((AbstractComponent)sourceComponent, targetIndex);
					layouts.addComponent(targetComponent, sourceIndex);
				});
			});

			layouts.addComponent(contentContainer);
		}
		container.setContent(layouts);
	}

	private void createDragSource(int i, AbstractComponent container) {
		DragSourceExtension<AbstractComponent> dragSource = new DragSourceExtension<>(container);
		dragSource.setEffectAllowed(EffectAllowed.MOVE);
		dragSource.setDataTransferText("Reordering " + i);
	}

	private void reorderingFields() {
		VerticalLayout layouts = new VerticalLayout();
		for(int i = 0; i < new Random().nextInt((10) + 1) + 5; i++) {
			layouts.addComponent(new TextField(i + ""));
		}
		container.setContent(layouts);
	}

	private void reorderingFieldsLayouts() {
		VerticalLayout layouts = new VerticalLayout();
		for(int i = 0; i < 2; i++) {
			layouts.addComponent(new HorizontalLayout(new TextField(i + "")));
		}
		container.setContent(layouts);
	}
}