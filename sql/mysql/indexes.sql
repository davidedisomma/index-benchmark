
CREATE UNIQUE INDEX /*i*/user_name ON /*_*/user (user_name);
CREATE INDEX /*i*/user_email_token ON /*_*/user (user_email_token);

CREATE UNIQUE INDEX /*i*/ug_user_group ON /*_*/user_groups (ug_user,ug_group);
CREATE INDEX /*i*/ug_group ON /*_*/user_groups (ug_group);

CREATE INDEX /*i*/un_user_id ON /*_*/user_newtalk (user_id);
CREATE INDEX /*i*/un_user_ip ON /*_*/user_newtalk (user_ip);

CREATE UNIQUE INDEX /*i*/user_properties_user_property ON /*_*/user_properties (up_user,up_property);
CREATE INDEX /*i*/user_properties_property ON /*_*/user_properties (up_property);

CREATE UNIQUE INDEX /*i*/name_title ON /*_*/page (page_namespace,page_title);
CREATE INDEX /*i*/page_random ON /*_*/page (page_random);
CREATE INDEX /*i*/page_len ON /*_*/page (page_len);

CREATE UNIQUE INDEX /*i*/rev_page_id ON /*_*/revision (rev_page, rev_id);
CREATE INDEX /*i*/rev_timestamp ON /*_*/revision (rev_timestamp);
CREATE INDEX /*i*/page_timestamp ON /*_*/revision (rev_page,rev_timestamp);
CREATE INDEX /*i*/user_timestamp ON /*_*/revision (rev_user,rev_timestamp);
CREATE INDEX /*i*/usertext_timestamp ON /*_*/revision (rev_user_text,rev_timestamp);

CREATE INDEX /*i*/name_title_timestamp ON /*_*/archive (ar_namespace,ar_title,ar_timestamp);
CREATE INDEX /*i*/ar_usertext_timestamp ON /*_*/archive (ar_user_text,ar_timestamp);
CREATE INDEX /*i*/ar_revid ON /*_*/archive (ar_rev_id);

CREATE UNIQUE INDEX /*i*/pl_from ON /*_*/pagelinks (pl_from,pl_namespace,pl_title);
CREATE UNIQUE INDEX /*i*/pl_namespace ON /*_*/pagelinks (pl_namespace,pl_title,pl_from);

CREATE UNIQUE INDEX /*i*/tl_from ON /*_*/templatelinks (tl_from,tl_namespace,tl_title);
CREATE UNIQUE INDEX /*i*/tl_namespace ON /*_*/templatelinks (tl_namespace,tl_title,tl_from);

CREATE UNIQUE INDEX /*i*/il_from ON /*_*/imagelinks (il_from,il_to);
CREATE UNIQUE INDEX /*i*/il_to ON /*_*/imagelinks (il_to,il_from);

CREATE UNIQUE INDEX /*i*/cl_from ON /*_*/categorylinks (cl_from,cl_to);

-- We always sort within a given category, and within a given type.  FIXME:
-- Formerly this index didn't cover cl_type (since that didn't exist), so old
-- callers won't be using an index: fix this?
CREATE INDEX /*i*/cl_sortkey ON /*_*/categorylinks (cl_to,cl_type,cl_sortkey,cl_from);

-- Not really used?
CREATE INDEX /*i*/cl_timestamp ON /*_*/categorylinks (cl_to,cl_timestamp);

-- For finding rows with outdated collation
CREATE INDEX /*i*/cl_collation ON /*_*/categorylinks (cl_collation);

CREATE UNIQUE INDEX /*i*/cat_title ON /*_*/category (cat_title);

-- For Special:Mostlinkedcategories
CREATE INDEX /*i*/cat_pages ON /*_*/category (cat_pages);

CREATE INDEX /*i*/el_from ON /*_*/externallinks (el_from, el_to(40));
CREATE INDEX /*i*/el_to ON /*_*/externallinks (el_to(60), el_from);
CREATE INDEX /*i*/el_index ON /*_*/externallinks (el_index(60));

CREATE UNIQUE INDEX /*i*/eu_external_id ON /*_*/external_user (eu_external_id);

CREATE UNIQUE INDEX /*i*/ll_from ON /*_*/langlinks (ll_from, ll_lang);
CREATE INDEX /*i*/ll_lang ON /*_*/langlinks (ll_lang, ll_title);

CREATE UNIQUE INDEX /*i*/iwl_from ON /*_*/iwlinks (iwl_from, iwl_prefix, iwl_title);
CREATE UNIQUE INDEX /*i*/iwl_prefix_title_from ON /*_*/iwlinks (iwl_prefix, iwl_title, iwl_from);

-- Pointless index to assuage developer superstitions
CREATE UNIQUE INDEX /*i*/ss_row_id ON /*_*/site_stats (ss_row_id);

-- Unique index to support "user already blocked" messages
-- Any new options which prevent collisions should be included
CREATE UNIQUE INDEX /*i*/ipb_address ON /*_*/ipblocks (ipb_address(255), ipb_user, ipb_auto, ipb_anon_only);

CREATE INDEX /*i*/ipb_user ON /*_*/ipblocks (ipb_user);
CREATE INDEX /*i*/ipb_range ON /*_*/ipblocks (ipb_range_start(8), ipb_range_end(8));
CREATE INDEX /*i*/ipb_timestamp ON /*_*/ipblocks (ipb_timestamp);
CREATE INDEX /*i*/ipb_expiry ON /*_*/ipblocks (ipb_expiry);

CREATE INDEX /*i*/img_usertext_timestamp ON /*_*/image (img_user_text,img_timestamp);
-- Used by Special:ListFiles for sort-by-size
CREATE INDEX /*i*/img_size ON /*_*/image (img_size);
-- Used by Special:Newimages and Special:ListFiles
CREATE INDEX /*i*/img_timestamp ON /*_*/image (img_timestamp);
-- Used in API and duplicate search
CREATE INDEX /*i*/img_sha1 ON /*_*/image (img_sha1);

CREATE INDEX /*i*/oi_usertext_timestamp ON /*_*/oldimage (oi_user_text,oi_timestamp);
CREATE INDEX /*i*/oi_name_timestamp ON /*_*/oldimage (oi_name,oi_timestamp);
-- oi_archive_name truncated to 14 to avoid key length overflow
CREATE INDEX /*i*/oi_name_archive_name ON /*_*/oldimage (oi_name,oi_archive_name(14));
CREATE INDEX /*i*/oi_sha1 ON /*_*/oldimage (oi_sha1);

-- pick out by image name
CREATE INDEX /*i*/fa_name ON /*_*/filearchive (fa_name, fa_timestamp);
-- pick out dupe files
CREATE INDEX /*i*/fa_storage_group ON /*_*/filearchive (fa_storage_group, fa_storage_key);
-- sort by deletion time
CREATE INDEX /*i*/fa_deleted_timestamp ON /*_*/filearchive (fa_deleted_timestamp);
-- sort by uploader
CREATE INDEX /*i*/fa_user_timestamp ON /*_*/filearchive (fa_user_text,fa_timestamp);

CREATE INDEX /*i*/rc_timestamp ON /*_*/recentchanges (rc_timestamp);
CREATE INDEX /*i*/rc_namespace_title ON /*_*/recentchanges (rc_namespace, rc_title);
CREATE INDEX /*i*/rc_cur_id ON /*_*/recentchanges (rc_cur_id);
CREATE INDEX /*i*/new_name_timestamp ON /*_*/recentchanges (rc_new,rc_namespace,rc_timestamp);
CREATE INDEX /*i*/rc_ip ON /*_*/recentchanges (rc_ip);
CREATE INDEX /*i*/rc_ns_usertext ON /*_*/recentchanges (rc_namespace, rc_user_text);
CREATE INDEX /*i*/rc_user_text ON /*_*/recentchanges (rc_user_text, rc_timestamp);

CREATE UNIQUE INDEX /*i*/wl_user ON /*_*/watchlist (wl_user, wl_namespace, wl_title);
CREATE INDEX /*i*/namespace_title ON /*_*/watchlist (wl_namespace, wl_title);

CREATE UNIQUE INDEX /*i*/math_inputhash ON /*_*/math (math_inputhash);

CREATE UNIQUE INDEX /*i*/si_page ON /*_*/searchindex (si_page);

CREATE UNIQUE INDEX /*i*/iw_prefix ON /*_*/interwiki (iw_prefix);

CREATE INDEX /*i*/qc_type ON /*_*/querycache (qc_type,qc_value);

CREATE INDEX /*i*/exptime ON /*_*/objectcache (exptime);

CREATE UNIQUE INDEX /*i*/tc_url_idx ON /*_*/transcache (tc_url);

CREATE INDEX /*i*/type_time ON /*_*/logging (log_type, log_timestamp);
CREATE INDEX /*i*/user_time ON /*_*/logging (log_user, log_timestamp);
CREATE INDEX /*i*/page_time ON /*_*/logging (log_namespace, log_title, log_timestamp);
CREATE INDEX /*i*/times ON /*_*/logging (log_timestamp);
CREATE INDEX /*i*/log_user_type_time ON /*_*/logging (log_user, log_type, log_timestamp);
CREATE INDEX /*i*/log_page_id_time ON /*_*/logging (log_page,log_timestamp);

CREATE UNIQUE INDEX /*i*/ls_field_val ON /*_*/log_search (ls_field,ls_value,ls_log_id);
CREATE INDEX /*i*/ls_log_id ON /*_*/log_search (ls_log_id);

CREATE INDEX /*i*/tb_page ON /*_*/trackbacks (tb_page);

CREATE INDEX /*i*/job_cmd ON /*_*/job (job_cmd, job_namespace, job_title, job_params(128));

CREATE UNIQUE INDEX /*i*/qci_type ON /*_*/querycache_info (qci_type);

CREATE INDEX /*i*/rd_ns_title ON /*_*/redirect (rd_namespace,rd_title,rd_from);

CREATE INDEX /*i*/qcc_type ON /*_*/querycachetwo (qcc_type,qcc_value);
CREATE INDEX /*i*/qcc_title ON /*_*/querycachetwo (qcc_type,qcc_namespace,qcc_title);
CREATE INDEX /*i*/qcc_titletwo ON /*_*/querycachetwo (qcc_type,qcc_namespacetwo,qcc_titletwo);

CREATE UNIQUE INDEX /*i*/pr_pagetype ON /*_*/page_restrictions (pr_page,pr_type);
CREATE INDEX /*i*/pr_typelevel ON /*_*/page_restrictions (pr_type,pr_level);
CREATE INDEX /*i*/pr_level ON /*_*/page_restrictions (pr_level);
CREATE INDEX /*i*/pr_cascade ON /*_*/page_restrictions (pr_cascade);

CREATE UNIQUE INDEX /*i*/pt_namespace_title ON /*_*/protected_titles (pt_namespace,pt_title);
CREATE INDEX /*i*/pt_timestamp ON /*_*/protected_titles (pt_timestamp);

CREATE UNIQUE INDEX /*i*/pp_page_propname ON /*_*/page_props (pp_page,pp_propname);

CREATE UNIQUE INDEX /*i*/change_tag_rc_tag ON /*_*/change_tag (ct_rc_id,ct_tag);
CREATE UNIQUE INDEX /*i*/change_tag_log_tag ON /*_*/change_tag (ct_log_id,ct_tag);
CREATE UNIQUE INDEX /*i*/change_tag_rev_tag ON /*_*/change_tag (ct_rev_id,ct_tag);
-- Covering index, so we can pull all the info only out of the index.
CREATE INDEX /*i*/change_tag_tag_id ON /*_*/change_tag (ct_tag,ct_rc_id,ct_rev_id,ct_log_id);

CREATE UNIQUE INDEX /*i*/tag_summary_rc_id ON /*_*/tag_summary (ts_rc_id);
CREATE UNIQUE INDEX /*i*/tag_summary_log_id ON /*_*/tag_summary (ts_log_id);
CREATE UNIQUE INDEX /*i*/tag_summary_rev_id ON /*_*/tag_summary (ts_rev_id);

CREATE INDEX /*i*/lc_lang_key ON /*_*/l10n_cache (lc_lang, lc_key);

CREATE UNIQUE INDEX /*i*/mr_resource_lang ON /*_*/msg_resource (mr_resource, mr_lang);
CREATE UNIQUE INDEX /*i*/mrl_message_resource ON /*_*/msg_resource_links (mrl_message, mrl_resource);

CREATE UNIQUE INDEX /*i*/md_module_skin ON /*_*/module_deps (md_module, md_skin);
