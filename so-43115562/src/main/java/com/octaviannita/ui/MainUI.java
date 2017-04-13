package com.octaviannita.ui;

import com.octaviannita.service.DataSource;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Apr 11, 2017
 */
@Theme("valo")
@Title("[SO Question #43115562]")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout content = new VerticalLayout();
        this.setContent(content);
        content.setSpacing(true);
        content.setMargin(true);

        final Tree desRacinesEtDesAiles = new Tree("Des racines et des ailes");
        loadTree(desRacinesEtDesAiles, null, new DataSource().getTestData().getDocumentElement());

        content.addComponent(desRacinesEtDesAiles);
    }

    /**
     * If <code>parentId</code> is <code>null</code> then the <code>element</code>'s
     * name or value gets added directly under <code>tree</code>.
     */
    private void loadTree(Tree tree, Object parentId, Node element) {
        if (tree == null || element == null) {
            return;
        }

        // 1. We add to the tree what's given to us as argument:

        final String itemId =
            (element.getNodeType() == Node.TEXT_NODE ? element.getNodeValue() : element.getNodeName()).trim();
        if (itemId.length() == 0) {
            return; // skip eventual white spaces...
        }
        tree.addItem(itemId); // a tree item's label defaults to its ID

        // 2. If a parent ID is also provided as argument, we set that as parent for the newly added item

        if (parentId != null) {
            tree.setParent(itemId, parentId);
        }

        // 3. We then _recursively_ add to the tree every child of the given element (if any):

        NodeList children = element.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            loadTree(tree, itemId, children.item(i));
        }

        // 4. Optionally we can expand an item if it has any children or force hiding the arrow if not:

        if (children.getLength() > 0) {
            tree.expandItem(itemId);
        } else {
            tree.setChildrenAllowed(itemId, false);
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {}
}
