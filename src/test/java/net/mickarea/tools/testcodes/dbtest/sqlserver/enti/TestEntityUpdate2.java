package net.mickarea.tools.testcodes.dbtest.sqlserver.enti;

import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.tools.models.DatabasePoolManager;
import net.mickarea.tools.testcodes.dbtest.sqlserver.beans.Student;
import net.mickarea.tools.utils.ListUtil;
import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.StrUtil;
import net.mickarea.tools.utils.database.DBOperator;
import net.mickarea.tools.utils.database.DBSQLInjectionUtil;

public class TestEntityUpdate2 {

	public static void main(String[] args) {
		
		// 创建连接池管理器
		DatabasePoolManager mana = DatabasePoolManager.getInstance();
		// sqlserver 数据库的连接池
		DataSource sqlserverDS = mana.getDataPoolByName("jar_sqlserver");
		//
		DBOperator db = new DBOperator(sqlserverDS, 
				DBSQLInjectionUtil.DBTYPE_MS_SQLSERVER);
		//
		StringBuffer sb = new StringBuffer();
		List<Student> stus = db.queryEntity(Student.class, sb);
		//
		if(StrUtil.isEmptyString(sb.toString())) {
			Stdout.pl("查询 学生信息成功");
			if(!ListUtil.isEmptyList(stus)) {
				//修改信息
				for(Student s: stus) {
					s.setStuName("修改了...222");
					s.setUTime(LocalDateTime.now());
				}
				//执行修改
				Stdout.fpl("学生信息数量为 %s。开始修改 >>>", stus.size());
				String msg2 = db.updateEntity(stus, null);
				//String msg2 = db.updateEntity(stus, "stuName, uTime");
				Stdout.fpl("异常信息：%s", msg2);
				Stdout.pl("修改结束 <<<");
			}else {
				Stdout.pl("学生信息数量为 0。不执行修改");
			}
		}else {
			Stdout.pl("查询 学生信息失败");
		}
		// 销毁 管理器中的 连接池，断开数据库中的 连接，释放资源
		mana.destroyDataPools();

	}

}
