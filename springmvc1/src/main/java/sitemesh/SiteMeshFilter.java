package sitemesh;

import javax.servlet.annotation.WebFilter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
/*
 * sitemesh 설정 : 화면의 공통부분을 설정
 * 1. pom.xml에 sitemesh관련부분 설정=> sitemesh-3.0.1.jar파일 생성
 * 2. layout 폴더에 kiclayout.jsp 파일 생성.
 * 3. @WebFilter("/*") : 모든 요청시 해당 필터가 실행되도록 설정.
 */
@WebFilter("/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter{
	@Override
	protected void applyCustomConfiguration
					(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPath("/*", "/layout/kiclayout.jsp");
	}
}
