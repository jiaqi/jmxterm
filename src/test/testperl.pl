#!/usr/bin/perl

my $jar = shift;
die "Please specify jar file in command" unless $jar;

open JMX, "| java -jar $jar";

print JMX "help \n";
print JMX "open localhost:9991\n";
print JMX "domains\n";
print JMX "close\n";

close JMX;

