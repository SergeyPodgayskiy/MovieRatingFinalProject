package by.epam.movierating.controller.util;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author serge
 *         11.07.2017.
 */
public final class RoleAndCommandsParserUtil {
    private static final Logger logger = Logger.getLogger(RoleAndCommandsParserUtil.class);
    private static final RoleAndCommandsParserUtil INSTANCE = new RoleAndCommandsParserUtil();
    private static final String ROLES_RIGHTS_XML_PATH = "/rolesAndRights.xml";
    private static final String COMMAND = "command";

    private RoleAndCommandsParserUtil() {
    }

    public static RoleAndCommandsParserUtil getInstance() {
        return INSTANCE;
    }

    public Map<String, List<String>> getRoleAndCommandsMap(String roleName) {
        InputSource inputSource = new InputSource(getClass().getResourceAsStream(ROLES_RIGHTS_XML_PATH));
        DOMParser domParser = new DOMParser();
        Map<String, List<String>> roleAndCommandsMap = new HashMap<>();

        try {
            domParser.parse(inputSource);
        } catch (SAXException | IOException e) {
            logger.error(e);
        }
        Document document = domParser.getDocument();
        Element root = document.getDocumentElement();
        List<String> allowedCommands = getAllowedCommands(roleName, root);
        roleAndCommandsMap.put(roleName, allowedCommands);

        return roleAndCommandsMap;
    }

    private List<String> getAllowedCommands(String roleName, Element root) {
        List<String> allowedCommands = new ArrayList<>();
        NodeList nodeList = root.getElementsByTagName(roleName.toLowerCase());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element nodeListElement = (Element) nodeList.item(i);
            List<Element> allowedElements = getChildren(nodeListElement, COMMAND);
            for (Element element : allowedElements) {
                allowedCommands.add(element.getTextContent().trim());
            }
        }
        return allowedCommands;
    }

    private List<Element> getChildren(Element element, String childName) {
        NodeList nodeList = element.getElementsByTagName(childName);
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element child = (Element) nodeList.item(i);
            elements.add(child);
        }
        return elements;
    }
}
