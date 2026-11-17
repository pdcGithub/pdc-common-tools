/******************************************************************************************************

This file "OrderParamListTester1.java" is part of project "pdc-common-database" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.models.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import net.mickarea.tools.beans.tables.Student;
import net.mickarea.tools.beans.views.VStudent;
import net.mickarea.tools.beans.virtualentities.VOStudent;
import net.mickarea.tools.utils.PatternUtil;
import net.mickarea.tools.utils.Stdout;

/**
 * 这是关于 net.mickarea.tools.models.query.OrderParamList 的单元测试
 * @author Michael Pang (Dongcan Pang)
 * @since 2026年11月17日
 */
public class OrderParamListTester1 {
	
	/**
	 * 这里测试的是无参构造函数 OrderParamList()
	 */
	@Test
	void testConstructor1() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		OrderParamList list1 = new OrderParamList();
		
		// 查看初始状态
		assertEquals(0, list1.getSize());
		assertEquals(null, list1.getEntityClassName());
		
		// 比对 设置值后的状态。如果值不符合要求，它不会设置进去
		OrderParamList list2 = new OrderParamList();
		list2.setEntityClassName(Student.class);
		assertEquals(Student.class, list2.getEntityClassName());
		list2.setEntityClassName(VStudent.class);
		assertEquals(VStudent.class, list2.getEntityClassName());
		list2.setEntityClassName(VOStudent.class);
		assertEquals(VOStudent.class, list2.getEntityClassName());
		list2.setEntityClassName(null);  // 如果不是 实体类，则不会修改
		assertEquals(VOStudent.class, list2.getEntityClassName());
		list2.setEntityClassName(this.getClass());
		assertEquals(VOStudent.class, list2.getEntityClassName());
		
	}
	
	/**
	 * 这里测试的是有参构造函数 OrderParamList(Class<?> entityClassName)
	 */
	@Test
	void testConstructor2() {
		
		// 测试构造函数时，将会顺带测试 getter 和 setter 
		
		OrderParamList list1 = new OrderParamList(null);
		OrderParamList list2 = new OrderParamList(this.getClass());
		OrderParamList list3 = new OrderParamList(Student.class);
		OrderParamList list4 = new OrderParamList(VStudent.class);
		OrderParamList list5 = new OrderParamList(VOStudent.class);
		
		// 判断初始设置是否成功
		assertEquals(null, list1.getEntityClassName());
		assertEquals(null, list2.getEntityClassName());
		assertEquals(Student.class, list3.getEntityClassName());
		assertEquals(VStudent.class, list4.getEntityClassName());
		assertEquals(VOStudent.class, list5.getEntityClassName());
		
	}
	
	/**
	 * 这里测试的是 add 方法
	 */
	@Test
	void testAddParam() {
		
		// 初始化一个对象
		OrderParamList list1 = new OrderParamList();
		
		// 初始状态
		assertEquals(0, list1.getSize());
		
		// 首先是添加异常的对象
		list1.addParam(null);
		assertEquals(0, list1.getSize());
		list1.addParam(new OrderParam());
		assertEquals(0, list1.getSize());
		list1.addParam(new OrderParam(null));
		assertEquals(0, list1.getSize());
		list1.addParam(new OrderParam(null, null));
		assertEquals(0, list1.getSize());
		list1.addParam(new OrderParam("", null));
		assertEquals(0, list1.getSize());
		list1.addParam(new OrderParam("  ", null));
		assertEquals(0, list1.getSize());
		
		// 然后是添加正常的对象
		list1.addParam(new OrderParam("test1")); // 排序对象，默认是有 asc 操作符的。
		assertEquals(1, list1.getSize());
		list1.addParam(new OrderParam("test2", OrderParam.OPT_DESC));
		assertEquals(2, list1.getSize());
		
	}
	
	/**
	 * 这里测试的是 多个 remove 方法
	 */
	@Test
	void testRemoveParam() {
		
		// removeParam(int i) 
		OrderParam o1 = new OrderParam("gender", OrderParam.OPT_DESC);
		OrderParam o2 = new OrderParam("stuNo");
		OrderParam o3 = new OrderParam("address", OrderParam.OPT_DESC, "2k");
		OrderParam o4 = new OrderParam("birthDate");
		
		OrderParamList list1 = new OrderParamList(Student.class);
		list1.addParam(o1);
		list1.addParam(o2);
		list1.addParam(o3);
		list1.addParam(o4);
		
		list1.removeParam(1000);
		assertEquals(4, list1.getSize());
		list1.removeParam(-1);
		assertEquals(4, list1.getSize()); // 不在长度范围内，不会删除
		list1.removeParam(3);
		assertEquals(3, list1.getSize());
		list1.removeParam(2);
		assertEquals(2, list1.getSize());
		list1.removeParam(1);
		assertEquals(1, list1.getSize());
		list1.removeParam(0);
		assertEquals(0, list1.getSize());
		
		// removeParam(String paramName)
		OrderParamList list2 = new OrderParamList(Student.class);
		list2.addParam(o1);
		list2.addParam(o2);
		list2.addParam(o3);
		list2.addParam(o4);
		
		list2.removeParam((String)null);
		assertEquals(4, list2.getSize());
		list2.removeParam("");
		assertEquals(4, list2.getSize());
		list2.removeParam("  ");
		assertEquals(4, list2.getSize());
		list2.removeParam(" aaaa ");
		assertEquals(4, list2.getSize()); // 非正常的名称不会删除
		list2.removeParam("  gender  ");
		assertEquals(3, list2.getSize());
		list2.removeParam("  stuNo  ");
		assertEquals(2, list2.getSize());
		list2.removeParam("  address");
		assertEquals(1, list2.getSize());
		list2.removeParam("birthDate");
		assertEquals(0, list2.getSize());
		
		// removeParam(OrderParam order)
		OrderParamList list3 = new OrderParamList(Student.class);
		list3.addParam(o1);
		list3.addParam(o2);
		list3.addParam(o3);
		list3.addParam(o4);
		
		assertEquals(4, list3.getSize());
		
		list3.removeParam(o4);  // 首先是 引用匹配 测试
		assertEquals(3, list3.getSize());
		list3.removeParam(o3);
		assertEquals(2, list3.getSize());
		list3.removeParam(o2);
		assertEquals(1, list3.getSize());
		list3.removeParam(o1);
		assertEquals(0, list3.getSize());
		
		list3.addParam(o1); // 再次填充
		list3.addParam(o2);
		list3.addParam(o3);
		list3.addParam(o4);
		
		list3.removeParam(new OrderParam("gender", OrderParam.OPT_DESC)); // 然后是 参数名和操作符匹配
		assertEquals(3, list3.getSize());
		list3.removeParam(new OrderParam("stuNo"));
		assertEquals(2, list3.getSize());
		list3.removeParam(new OrderParam("address", OrderParam.OPT_DESC, "2k"));
		assertEquals(1, list3.getSize());
		list3.removeParam(new OrderParam("birthDate"));
		assertEquals(0, list3.getSize());
		
		// removeAll()
		OrderParamList list4 = new OrderParamList(Student.class);
		list4.addParam(o1);
		list4.addParam(o2);
		list4.addParam(o3);
		list4.addParam(o4);
		
		// 当前数量
		assertEquals(4, list4.getSize());
		
		// 删除全部，并匹配数量
		list4.removeAll();
		assertEquals(0, list4.getSize());
		
	}
	
	/**
	 * 这里测试的是 sql 排序字符串
	 */
	@Test
	void testToSqlString() {
		
		// 对于 sql 处理 主要是生成 k.id asc, k.name desc 这样的东西。
		
		// ===================== 空的排序条件列表
		
		OrderParamList list1 = new OrderParamList();
		assertEquals("", list1.toSqlString());
		
		OrderParamList list2 = new OrderParamList(Student.class);
		assertEquals("", list2.toSqlString());
		
		// ===================== 有内容的排序测试
		OrderParam o1 = new OrderParam();
		OrderParam o2 = new OrderParam("");
		OrderParam o3 = new OrderParam("  ");
		OrderParam o4 = new OrderParam(" null ");
		OrderParam o5 = new OrderParam("test_id", OrderParam.OPT_DESC);
		OrderParam o6 = new OrderParam("test_name", OrderParam.OPT_DESC, "2k");
		
		OrderParamList list3 = new OrderParamList(Student.class);
		list3.addParam(o1);
		list3.addParam(o2);
		list3.addParam(o3);
		list3.addParam(o4);
		list3.addParam(o5);
		list3.addParam(o6);
		
		// 对于一些 异常的内容，是完全不会进入 list 中的。所以应该只有 2个字段
		assertEquals("test_id desc, 2k.test_name desc", list3.toSqlString());
		
		OrderParamList list4 = new OrderParamList();
		list4.addParam(new OrderParam("stuNo", OrderParam.OPT_DESC, "s").setClassName(Student.class));
		list4.addParam(new OrderParam("birthDate"));
		list4.addParam(new OrderParam("enrollDate", OrderParam.OPT_ASC, "aaa").setClassName(Student.class));
		
		// 对于实体翻译，只有设置了就行了
		assertEquals("s.stu_no desc, birthDate asc, aaa.enroll_date asc", list4.toSqlString());
		
		OrderParamList list5 = new OrderParamList(Student.class);
		list5.addParam(new OrderParam("stuNo", OrderParam.OPT_DESC, "s"));
		list5.addParam(new OrderParam("birthDate"));
		list5.addParam(new OrderParam("enrollDate", OrderParam.OPT_ASC, "aaa"));
		
		// 对于实体翻译，list 设置也行
		assertEquals("s.stu_no desc, birth_date asc, aaa.enroll_date asc", list5.toSqlString());
		
		OrderParamList list6 = new OrderParamList(Student.class);
		list6.addParam(new OrderParam("stuNo", OrderParam.OPT_DESC, "s"));
		
		// 最后测试一个 参数 的情况。看看是否会多个 逗号出来。
		assertEquals("s.stu_no desc", list6.toSqlString());
		
	}
	
	/**
	 * 这里测试的是 私有属性打印
	 */
	@Test
	void testToString() {
		
		OrderParam o1 = new OrderParam("gender", OrderParam.OPT_DESC);
		OrderParam o2 = new OrderParam("stuNo");
		OrderParam o3 = new OrderParam("address", OrderParam.OPT_DESC, "2k");
		OrderParam o4 = new OrderParam("birthDate");
		
		OrderParamList l1 = new OrderParamList();
		OrderParamList l2 = new OrderParamList(Student.class);
		OrderParamList l3 = new OrderParamList(Student.class);
		l3.addParam(o1);
		l3.addParam(o2);
		l3.addParam(o3);
		l3.addParam(o4);
		
		// 这里的输出，主要用于打印输出。观察信息是否有异常
		Stdout.pl(l1);
		Stdout.pl(l2);
		Stdout.pl(l3);
		
		// 定义一个正则，用于判断格式是否正确。(匹配时，忽略大小写)
		String regexp = "orderparamlist\\{\\(private\\).+(,[\\s]*\\(private\\).+)*\\}";
		
		// 判断
		assertTrue(PatternUtil.matches(regexp, l1.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, l2.toString(), Pattern.CASE_INSENSITIVE));
		assertTrue(PatternUtil.matches(regexp, l3.toString(), Pattern.CASE_INSENSITIVE));
		
	}
	
}
