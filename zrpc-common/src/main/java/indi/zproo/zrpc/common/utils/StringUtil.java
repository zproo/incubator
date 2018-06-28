package indi.zproo.zrpc.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: zproo
 * @create: 2018-06-07 20:17
 **/
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {

		if (str != null) {
			// 返回字符串副本，忽略前导空白和尾部空白
			str = str.trim();
		}
		return StringUtils.isEmpty(str);
	}

	/**
	 * 判断字符串是否为非空
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 分割固定格式的字符串
	 *
	 * @return
	 */
	public static String[] split(String str, String seperator) {
		return StringUtils.splitByWholeSeparator(str, seperator);
	}

}
