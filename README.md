trees
=====

_A Java library for tree data structures and computations_

 * Provides data structures for weighted trees.
 * Provides various operations on trees (such as extracting subtrees, tree union and intersection, and such).
 * Additional functionality specific to phylogenetic trees is available in [phyloutils](http://github.com/davidsoergel/phyloutils/).

Documentation
-------------

 * [API docs](http://davidsoergel.github.io/trees/)

Download
--------

[Maven](http://maven.apache.org/) is by far the easiest way to make use of dsutils.  Just add these to your pom.xml:
```xml
<repositories>
	<repository>
		<id>dev.davidsoergel.com releases</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/releases</url>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</repository>
	<repository>
		<id>dev.davidsoergel.com snapshots</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/snapshots</url>
		<releases>
			<enabled>false</enabled>
		</releases>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>com.davidsoergel</groupId>
		<artifactId>trees</artifactId>
		<version>1.031</version>
	</dependency>
</dependencies>
```

If you really want just the jar, you can get the [latest release](http://dev.davidsoergel.com/nexus/content/repositories/releases/com/davidsoergel/trees/) from the Maven repo; or get the [latest stable build](http://dev.davidsoergel.com/jenkins/job/trees/lastStableBuild/com.davidsoergel$trees/) from the build server.

