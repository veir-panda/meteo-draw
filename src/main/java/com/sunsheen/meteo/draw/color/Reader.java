package com.sunsheen.meteo.draw.color;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Reader {
	public static Boundaries ReadBoundary(String filename) {
		Boundaries b = null;
		BufferedReader fi = null;

		// D:\hearken_v4_2\workspace_analyse\meteo_analyse\resources\analyse\common\src\com\sunsheen\meteo\analyse\product\data
		try {
			fi = new BufferedReader(new FileReader(filename));
			String hasRead = "";
			hasRead = fi.readLine();
			String ss[] = hasRead.split(";");
			b = new Boundaries(ss.length);
			for (int i = 0; i < ss.length; i++) {
				String ss1[] = ss[i].split(",");
				Boundary b1 = new Boundary();
				for (int j = 0; j < ss1.length; j++) {
					b1.add(new BoundaryPoint(ss1[j]));
				}
				b.add(b1);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fi.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return b;
	}

	public static ColorCode ReadColorCode(String filepath, String configName) {
		try (InputStream is = new FileInputStream(filepath)){
			return ReadColorCode(is, configName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static ColorCode ReadColorCode(InputStream is, String configName) {
		ColorCode li = null;

		SAXReader saxReader = new SAXReader();
		try {
			// 根据根节点读取文档
			Document document = saxReader.read(is);
			Element nodes = document.getRootElement();
			Iterator it = nodes.elementIterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				if (element.getName().toUpperCase()
						.equals(configName.toUpperCase())) {
					int levelcnt = Integer.valueOf(element
							.attributeValue("levelcnt"));
					li = new ColorCode(levelcnt);

					Iterator eleIt = element.elementIterator();
					double v;
					while (eleIt.hasNext()) {
						Element e = (Element) eleIt.next();
						if (e.getName().equals("Unit")) {
							li.setUnit(e.getText());
						} else if (e.getName().equals("Lev")) {
							String ss[] = e.getText().split(";");
							li.add(ss[0], ss[1]);
						}
						// System.out.println(e.getName() + ": " + e.getText());
						//
						// Iterator attrIt = e.attributeIterator();
						// while (attrIt.hasNext()) {
						// Attribute a = (Attribute) attrIt.next();
						// System.out
						// .println(a.getName() + ":" + a.getValue());
						// }
					}
					li.addList();
					return li;
				}
			}
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		}
		return li;
	}

	public static List getListfromTxt(String filepath, String datanames,
			String splitchar) {
		BufferedReader fi = null;
		List datalist = new ArrayList();
		String cc[] = datanames.split(",");
		try {
			fi = new BufferedReader(new FileReader(filepath));
			String hasRead = "";
			while ((hasRead = fi.readLine()) != null) {
				String ss[] = hasRead.split(splitchar);
				HashMap d = new HashMap();
				for (int i = 0; i < ss.length; i++) {
					if (i < cc.length) {
						d.put(cc[i], ss[i]);
					}
				}
				datalist.add(d);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fi.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return datalist;
	}

	public static List[] getListsfromTxts(String[] filepath, String datanames,
			String splitchar) {
		List lists = new ArrayList();
		for (int j = 0; j < filepath.length; j++) {
			BufferedReader fi = null;
			List datalist = new ArrayList();
			String cc[] = datanames.split(",");
			try {
				fi = new BufferedReader(new FileReader(filepath[j]));
				String hasRead = "";
				while ((hasRead = fi.readLine()) != null) {
					String ss[] = hasRead.split(splitchar);
					HashMap d = new HashMap();
					for (int i = 0; i < ss.length; i++) {
						if (i < cc.length) {
							d.put(cc[i], ss[i]);
						}
					}
					datalist.add(d);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fi.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			lists.add(datalist);
		}

		List[] l = new List[lists.size()];
		for (int i = 0; i < lists.size(); i++) {
			l[i] = (List) lists.get(i);
		}
		return l;
	}

	public static void main(String[] args) {
		ColorCode li = Reader
				.ReadColorCode(
						"D:\\hearken_v4_2\\workspace_analyse\\meteo_analyse\\resources\\analyse\\common\\src\\com\\sunsheen\\meteo\\analyse\\product\\data\\LegendConfig.xml",
						"temp");

		li.print();
	}
}
