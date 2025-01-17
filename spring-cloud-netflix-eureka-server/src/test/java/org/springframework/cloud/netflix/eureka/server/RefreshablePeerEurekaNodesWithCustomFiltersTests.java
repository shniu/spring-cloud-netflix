/*
 * Copyright 2013-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.eureka.server;

import java.lang.reflect.Field;

import com.netflix.eureka.cluster.PeerEurekaNodes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration.RefreshablePeerEurekaNodes;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Yuxin Bai
 */
@Disabled // FIXME: 4.0
@SpringBootTest(classes = RefreshablePeerEurekaNodesWithCustomFiltersTests.Application.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, value = { "spring.application.name=eureka",
				"server.contextPath=/context", "management.security.enabled=false" })
class RefreshablePeerEurekaNodesWithCustomFiltersTests {

	@Autowired
	private PeerEurekaNodes peerEurekaNodes;

	@Test
	void testCustomPeerNodesShouldTakePrecedenceOverDefault() {
		assertThat(peerEurekaNodes instanceof RefreshablePeerEurekaNodes)
				.as("PeerEurekaNodes should be an instance of RefreshablePeerEurekaNodes").isTrue();

		ReplicationClientAdditionalFilters filters = getField(RefreshablePeerEurekaNodes.class,
				(RefreshablePeerEurekaNodes) peerEurekaNodes, "replicationClientAdditionalFilters");
		// assertThat(filters.getFilters())
		// .as("PeerEurekaNodes'should have only one filter set on
		// replicationClientAdditionalFilters").hasSize(1);
		// assertThat(filters.getFilters().iterator().next() instanceof
		// Application.CustomClientFilter)
		// .as("The type of the filter should be CustomClientFilter as user declared
		// so").isTrue();
	}

	private static <T, R> R getField(Class<T> clazz, T target, String fieldName) {
		Field field = ReflectionUtils.findField(clazz, fieldName);
		ReflectionUtils.makeAccessible(field);
		@SuppressWarnings("unchecked")
		R value = (R) ReflectionUtils.getField(field, target);
		return value;
	}

	@Configuration(proxyBeanMethods = false)
	@EnableAutoConfiguration
	@EnableEurekaServer
	protected static class Application {

		// FIXME: 4.0
		/*
		 * @Bean public ReplicationClientAdditionalFilters customFilters() { return new
		 * ReplicationClientAdditionalFilters(Collections.singletonList(new
		 * CustomClientFilter())); }
		 *
		 * protected class CustomClientFilter extends ClientFilter {
		 *
		 * @Override public ClientResponse handle(ClientRequest cr) throws
		 * ClientHandlerException { return getNext().handle(cr); }
		 *
		 * }
		 */

	}

}
